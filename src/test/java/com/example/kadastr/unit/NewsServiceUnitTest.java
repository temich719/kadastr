package com.example.kadastr.unit;

import com.example.kadastr.dao.CommentDAO;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.service.impl.NewsServiceImpl;
import com.example.kadastr.util.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsServiceUnitTest {

    @Mock
    private NewsDAO newsDAO;

    @Mock
    private CommentDAO commentDAO;

    @Mock
    private Mapper<News, NewsDto> newsMapper;

    @Mock
    private AuthHelper authHelper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private NewsDto newsDto;
    private News news;
    private UUID newsId;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        newsId = UUID.randomUUID();
        newsDto = new NewsDto();
        newsDto.setTitle("Test Title");
        newsDto.setText("Test Text");

        comment = new Comment();
        comment.setText("Test Comment");

        news = new News();
        news.setUuid(newsId);
        news.setTitle("Test Title");
        news.setText("Test Text");
        Set<Comment> comments = new HashSet<>();
        comments.add(comment);
        news.setComments(comments);
    }

    @Test
    public void testGetNewsById_Success() throws NoSuchIdException {
        when(newsDAO.findById(newsId)).thenReturn(Optional.of(news));
        when(commentDAO.findCommentByIdNews(eq(newsId), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(comment)));

        when(newsMapper.mapToDto(news)).thenReturn(newsDto);

        NewsDto result = newsService.getNewsById(newsId, 0, 10);

        assertNotNull(result);
        assertEquals(newsDto.getTitle(), result.getTitle());
        assertEquals(newsDto.getText(), result.getText());
    }

    @Test
    public void testGetNewsById_NotFound() {
        when(newsDAO.findById(newsId)).thenReturn(Optional.empty());

        assertThrows(NoSuchIdException.class, () -> newsService.getNewsById(newsId, 0, 10));
    }

    @Test
    public void testGetNewsList_Success() {
        when(newsDAO.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Arrays.asList(news)));
        when(newsMapper.mapToDto(news)).thenReturn(newsDto);

        List<NewsDto> result = newsService.getNewsList(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(newsDto.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testFindNewsByTextAndTitle_Success() {
        when(newsDAO.findByTextAndTitle("Test", "Title")).thenReturn(Arrays.asList(news));
        when(newsMapper.mapToDto(news)).thenReturn(newsDto);

        List<NewsDto> result = newsService.findNewsByTextAndTitle("Test", "Title");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(newsDto.getTitle(), result.get(0).getTitle());
    }

    @Test
    public void testCreateNews_Success() throws AuthException {
        when(authHelper.getCurrentUserUUID()).thenReturn(UUID.randomUUID());

        when(newsMapper.mapToModel(newsDto)).thenReturn(news);

        newsService.createNews(newsDto);

        verify(newsDAO, times(1)).save(news);
    }

    @Test
    public void testUpdateNews_Success() throws NoSuchIdException, IllegalControlException, AuthException {
        when(newsDAO.findById(newsId)).thenReturn(Optional.of(news));
        when(authHelper.checkUserControlOnEntity(news)).thenReturn(true);
        when(authHelper.getCurrentUserUUID()).thenReturn(UUID.randomUUID());

        newsService.updateNews(newsId, newsDto);

        verify(newsDAO, times(1)).save(news);
    }

    @Test
    public void testDeleteNews_Success() throws NoSuchIdException, IllegalControlException {
        when(newsDAO.findById(newsId)).thenReturn(Optional.of(news));
        when(authHelper.checkUserControlOnEntity(news)).thenReturn(true);

        newsService.deleteNews(newsId);

        verify(newsDAO, times(1)).delete(news);
    }

}
