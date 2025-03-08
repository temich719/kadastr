package com.example.kadastr.service;

import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.NoSuchIdException;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    /**
     * finds comment by uuid
     * @param uuid uuid of comment that needs to be found
     * @return comment DTO
     * @throws NoSuchIdException when comment with such uuid doesn't exist
     */
    CommentDto getCommentById(UUID uuid) throws NoSuchIdException;

    /**
     * find all comments
     * @return list of all comments
     */
    List<CommentDto> getCommentsList();

    /**
     * creates new comment
     * @param newsId id of news to which comment will be attached
     * @param commentDto DTO of comment that needs to be created
     * @throws NoSuchIdException when news with given newsId doesn't exist
     * @throws AuthException when can't assign uuid of creator 'cause nobody is authenticated
     */
    void createComment(UUID newsId, CommentDto commentDto) throws NoSuchIdException, AuthException;

    /**
     * updates comment
     * @param uuid is uuid of comment that needs to be updated
     * @param commentDto new state of comment
     * @throws NoSuchIdException when comment with given uuid doesn't exist
     * @throws IllegalControlException when authenticated user try to update comment that doesn't belong him
     */
    void updateComment(UUID uuid, CommentDto commentDto) throws NoSuchIdException, IllegalControlException;

    /**
     * deletes comment
     * @param uuid is uuid of comment that needs to be deleted
     * @throws NoSuchIdException when comment with given uuid doesn't exist
     * @throws IllegalControlException when authenticated user try to delete comment that doesn't belong him
     */
    void deleteComment(UUID uuid) throws NoSuchIdException, IllegalControlException;

}
