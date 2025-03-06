package com.example.kadastr.service.impl;

import com.example.kadastr.dao.CommentDAO;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.service.NewsService;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDAO newsDAO;
    private final CommentDAO commentDAO;
    private final Mapper<News, NewsDto> newsMapper;

    @Autowired
    public NewsServiceImpl(NewsDAO newsDAO, CommentDAO commentDAO, Mapper<News, NewsDto> newsMapper) {
        this.newsDAO = newsDAO;
        this.commentDAO = commentDAO;
        this.newsMapper = newsMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public NewsDto getNewsById(UUID uuid, int page, int commentsSize) throws NoSuchIdException {
        News news = getPureNewsById(uuid);
        Page<Comment> commentsPage = commentDAO.findCommentByIdNews(uuid, PageRequest.of(page, commentsSize));
        news.setComments(commentsPage.get().collect(Collectors.toSet()));
        return newsMapper.mapToDto(news);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NewsDto> getNewsList(int page, int size) {
        return newsDAO.findAll(PageRequest.of(page, size)).get().map(newsMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<NewsDto> findNewsByTextAndTitle(String text, String title) {
        return newsDAO.findByTextAndTitle(text, title).stream().map(newsMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createNews(NewsDto newsDto) {
        newsDAO.save(newsMapper.mapToModel(newsDto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNews(UUID uuid, NewsDto newsDto) throws NoSuchIdException {
        News news = getPureNewsById(uuid);
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        news.setUpdatedById(newsDto.getUpdatedById());
        newsDAO.save(news);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteNews(UUID uuid) throws NoSuchIdException {
        newsDAO.delete(getPureNewsById(uuid));
    }

    private News getPureNewsById(UUID uuid) throws NoSuchIdException {
        return newsDAO.findById(uuid)
                .orElseThrow(
                        () -> new NoSuchIdException("News with such uuid = " + uuid + " doesn't exist")
                );
    }
}
