package com.example.kadastr.service.impl;

import com.example.kadastr.dao.CommentDAO;
import com.example.kadastr.dao.NewsDAO;
import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.model.Comment;
import com.example.kadastr.model.News;
import com.example.kadastr.service.CommentService;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDAO commentDAO;
    private final NewsDAO newsDAO;
    private final Mapper<Comment, CommentDto> commentMapper;

    @Autowired
    public CommentServiceImpl(CommentDAO commentDAO, NewsDAO newsDAO, Mapper<Comment, CommentDto> commentMapper) {
        this.commentDAO = commentDAO;
        this.newsDAO = newsDAO;
        this.commentMapper = commentMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CommentDto getCommentById(UUID uuid) throws NoSuchIdException {
        return commentMapper.mapToDto(getCommentModelById(uuid));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<CommentDto> getCommentsList() {
        return commentDAO.findAll().stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createComment(UUID newsId, CommentDto commentDto) throws NoSuchIdException {
        News news = newsDAO.findById(newsId)
                .orElseThrow(
                        () -> new NoSuchIdException("News with uuid = " + newsId + " doesn't exist")
                );
        news.getComments().add(commentMapper.mapToModel(commentDto));
        newsDAO.save(news);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateComment(UUID uuid, CommentDto commentDto) throws NoSuchIdException {
        Comment comment = getCommentModelById(uuid);
        comment.setText(commentDto.getText());
        commentDAO.save(comment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteComment(UUID uuid) throws NoSuchIdException {
        getCommentModelById(uuid);
        commentDAO.deleteById(uuid);
    }

    private Comment getCommentModelById(UUID uuid) throws NoSuchIdException {
        return commentDAO.findById(uuid)
                .orElseThrow(
                        () -> new NoSuchIdException("Comment with uuid = " + uuid + " wasn't found")
                );
    }
}
