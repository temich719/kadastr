package com.example.kadastr.service;

import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.NoSuchIdException;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    CommentDto getCommentById(UUID uuid) throws NoSuchIdException;

    List<CommentDto> getCommentsList();

    void createComment(UUID newsId, CommentDto commentDto) throws NoSuchIdException;

    void updateComment(UUID uuid, CommentDto commentDto) throws NoSuchIdException;

    void deleteComment(UUID uuid) throws NoSuchIdException;

}
