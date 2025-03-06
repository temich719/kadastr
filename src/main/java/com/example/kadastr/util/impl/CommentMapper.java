package com.example.kadastr.util.impl;

import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.model.Comment;
import com.example.kadastr.util.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper implements Mapper<Comment, CommentDto> {

    @Override
    public Comment mapToModel(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setInsertedById(commentDto.getInsertedById());
        comment.setIdNews(commentDto.getIdNews());
        return comment;
    }

    @Override
    public CommentDto mapToDto(Comment comment) {
        return new CommentDto(comment.getText(), comment.getInsertedById(), comment.getIdNews());
    }
}
