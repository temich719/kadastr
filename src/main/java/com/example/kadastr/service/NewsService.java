package com.example.kadastr.service;

import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.NoSuchIdException;

import java.util.List;
import java.util.UUID;

public interface NewsService {

    /**
     * finds news by given uuid
     * @param uuid is news id that needs to be found
     * @param page number of attached comments page
     * @param commentsSize size of attached comments page
     * @return news with given id and list of comments with definite size and page
     * @throws NoSuchIdException when news with given uuid doesn't exist
     */
    NewsDto getNewsById(UUID uuid, int page, int commentsSize) throws NoSuchIdException;

    /**
     * finds all news
     * @param page number of news page
     * @param size size of news page
     * @return list of news with given page and its size
     */
    List<NewsDto> getNewsList(int page, int size);

    /**
     * finds news where text and title like input params
     * @param text text of news
     * @param title title of news
     * @return list of news where text and title like input params
     */
    List<NewsDto> findNewsByTextAndTitle(String text, String title);

    /**
     * creates new news
     * @param newsDto DTO of news that needs to be created
     * @throws AuthException when can't assign uuid of creator 'cause nobody is authenticated
     */
    void createNews(NewsDto newsDto) throws AuthException;

    /**
     * updates news
     * @param uuid is uuid of news that needs to be updated
     * @param newsDto new news state
     * @throws NoSuchIdException when news with given uuid doesn't exist
     * @throws IllegalControlException when authenticated user try to update news that doesn't belong him
     * @throws AuthException when can't assign uuid of person who want to update news 'cause nobody is authenticated
     */
    void updateNews(UUID uuid, NewsDto newsDto) throws NoSuchIdException, IllegalControlException, AuthException;

    /**
     * deletes news
     * @param uuid is uuid of news that needs to be deleted
     * @throws NoSuchIdException when news with given uuid doesn't exist
     * @throws IllegalControlException when authenticated user try to delete news that doesn't belong him
     */
    void deleteNews(UUID uuid) throws NoSuchIdException, IllegalControlException;

}
