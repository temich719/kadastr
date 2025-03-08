package com.example.kadastr.service.impl;

import com.example.kadastr.dao.CommentDAO;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.model.User;
import com.example.kadastr.security.util.AuthHelper;
import com.example.kadastr.service.NewsService;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDAO newsDAO;
    private final CommentDAO commentDAO;
    private final Mapper<News, NewsDto> newsMapper;
    private final AuthHelper authHelper;

    @Autowired
    public NewsServiceImpl(NewsDAO newsDAO, CommentDAO commentDAO, Mapper<News, NewsDto> newsMapper, AuthHelper authHelper) {
        this.newsDAO = newsDAO;
        this.commentDAO = commentDAO;
        this.newsMapper = newsMapper;
        this.authHelper = authHelper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "newsCache", key = "'news-' + #uuid + '-page-' + #page + '-size-' + #commentsSize")
    @Override
    public NewsDto getNewsById(UUID uuid, int page, int commentsSize) throws NoSuchIdException {
        News news = getPureNewsById(uuid);
        Page<Comment> commentsPage = commentDAO.findCommentByIdNews(uuid, PageRequest.of(page, commentsSize));
        news.setComments(commentsPage.get().collect(Collectors.toSet()));
        return newsMapper.mapToDto(news);
    }

    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "newsCache", key = "'page-' + #page + '-size-' + #size")
    @Override
    public List<NewsDto> getNewsList(int page, int size) {
        return newsDAO.findAll(PageRequest.of(page, size)).get().map(newsMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Cacheable(value = "newsCache", key = "'text-' + #text + '-title-' + #title")
    @Override
    public List<NewsDto> findNewsByTextAndTitle(String text, String title) {
        return newsDAO.findByTextAndTitle(text, title).stream().map(newsMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNews(NewsDto newsDto) throws AuthException {
        News news = newsMapper.mapToModel(newsDto);
        news.setInsertedById(authHelper.getCurrentUserUUID());
        newsDAO.save(news);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNews(UUID uuid, NewsDto newsDto) throws NoSuchIdException, IllegalControlException, AuthException {
        News news = getPureNewsById(uuid);
        if (authHelper.checkUserControlOnEntity(news)) {
            news.setTitle(newsDto.getTitle());
            news.setText(newsDto.getText());
            news.setUpdatedById(authHelper.getCurrentUserUUID());
            newsDAO.save(news);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteNews(UUID uuid) throws NoSuchIdException, IllegalControlException {
        News newsForDelete = getPureNewsById(uuid);
        if (authHelper.checkUserControlOnEntity(newsForDelete)) {
            newsDAO.delete(newsForDelete);
        }
    }

    private News getPureNewsById(UUID uuid) throws NoSuchIdException {
        return newsDAO.findById(uuid)
                .orElseThrow(
                        () -> new NoSuchIdException("News with such uuid = " + uuid + " doesn't exist")
                );
    }
}
