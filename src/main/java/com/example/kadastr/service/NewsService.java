package com.example.kadastr.service;

import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.NoSuchIdException;

import java.util.List;
import java.util.UUID;

public interface NewsService {

    NewsDto getNewsById(UUID uuid, int page, int commentsSize) throws NoSuchIdException;

    List<NewsDto> getNewsList(int page, int size);

    List<NewsDto> findNewsByTextAndTitle(String text, String title);

    void createNews(NewsDto newsDto);

    void updateNews(UUID uuid, NewsDto newsDto) throws NoSuchIdException;

    void deleteNews(UUID uuid) throws NoSuchIdException;

}
