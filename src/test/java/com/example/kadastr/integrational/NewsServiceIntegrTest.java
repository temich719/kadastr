package com.example.kadastr.integrational;

import com.example.kadastr.config.TestConfig;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.News;
import com.example.kadastr.model.Role;
import com.example.kadastr.model.User;
import com.example.kadastr.service.NewsService;
import com.example.kadastr.util.Mapper;
import com.example.kadastr.util.impl.CommentMapper;
import com.example.kadastr.util.impl.NewsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class NewsServiceIntegrTest {

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsDAO newsDAO;

    private UUID newsId;

    @BeforeEach
    void setUp() {
        News news = new News();
        news.setTitle("Test News");
        news.setText("This is a test news content.");
        news.setInsertedById(UUID.fromString("5cce18df-81a1-46b5-943e-f691dd59d806"));
        newsDAO.save(news);
        newsId = news.getUuid();

        User user = new User();
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        user.setRole(role);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                new User(), null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Test
    void testGetNewsById_NotFound() {
        UUID fakeId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> newsService.getNewsById(fakeId, 0, 10));
    }

    @Test
    void testGetNewsList() {
        List<NewsDto> newsList = newsService.getNewsList(0, 10);
        assertFalse(newsList.isEmpty());
        assertEquals(10, newsList.size());
    }

    @Test
    @Transactional
    void testCreateNews_Success() throws AuthException {
        NewsDto newNews = new NewsDto();
        newNews.setTitle("New News");
        newNews.setText("Some news content");
        newsService.createNews(newNews);

        List<NewsDto> newsList = newsService.getNewsList(0, 10);
        assertEquals(10, newsList.size());
    }

    @Test
    @Transactional
    void testUpdateNews_Success() {
        NewsDto updatedNews = new NewsDto();
        updatedNews.setTitle("Updated Title");
        updatedNews.setText("Updated Text");
        Mapper<News, NewsDto> newsNewsDtoMapper = new NewsMapper(new CommentMapper());
        News news = newsNewsDtoMapper.mapToModel(updatedNews);
        news.setUuid(newsId);
        newsDAO.save(news);

        Optional<News> updated = newsDAO.findById(newsId);
        assertTrue(updated.isPresent());
        assertEquals("Updated Title", updated.get().getTitle());
    }

    @Test
    void testUpdateNews_NotFound() {
        NewsDto updatedNews = new NewsDto();
        updatedNews.setTitle("Should not update");

        UUID fakeNewsId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> newsService.updateNews(fakeNewsId, updatedNews));
    }

    @Test
    @Transactional
    void testDeleteNews_Success() {
        NewsDto updatedNews = new NewsDto();
        updatedNews.setTitle("Updated Title");
        updatedNews.setText("Updated Text");
        Mapper<News, NewsDto> newsNewsDtoMapper = new NewsMapper(new CommentMapper());
        News news = newsNewsDtoMapper.mapToModel(updatedNews);
        news.setUuid(newsId);
        newsDAO.save(news);
        newsDAO.deleteById(newsId);
        assertFalse(newsDAO.findById(newsId).isPresent());
    }

    @Test
    void testDeleteNews_NotFound() {
        UUID fakeNewsId = UUID.randomUUID();
        assertThrows(NoSuchIdException.class, () -> newsService.deleteNews(fakeNewsId));
    }

}
