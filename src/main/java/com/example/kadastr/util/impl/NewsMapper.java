package com.example.kadastr.util.impl;

import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class NewsMapper implements Mapper<News, NewsDto> {

    private final Mapper<Comment, CommentDto> commentMapper;

    @Autowired
    public NewsMapper(Mapper<Comment, CommentDto> commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public News mapToModel(NewsDto newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        if (nonNull(newsDto.getInsertedById())) {
            news.setInsertedById(newsDto.getInsertedById());
        } else if (nonNull(newsDto.getUpdatedById())) {
            news.setUpdatedById(newsDto.getUpdatedById());
        }
        news.setComments(
                newsDto.getComments().stream()
                        .map(commentMapper::mapToModel).collect(Collectors.toSet())
        );
        return news;
    }

    @Override
    public NewsDto mapToDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setTitle(news.getTitle());
        newsDto.setText(news.getText());
        newsDto.setInsertedById(news.getInsertedById());
        newsDto.setUpdatedById(news.getUpdatedById());
        newsDto.setComments(
                news.getComments().stream()
                     .map(commentMapper::mapToDto).collect(Collectors.toList())
        );
        return newsDto;
    }
}
