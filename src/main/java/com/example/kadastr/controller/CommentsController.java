package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//todo add validator and norm ans
@RestController
@RequestMapping("/api/comments")
public class CommentsController extends AbstractController {

    private final CommentService commentService;

    @Autowired
    public CommentsController(AnswerMessageJson answerMessageJson, CommentService commentService) {
        super(answerMessageJson);
        this.commentService = commentService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments() {
        return commentService.getCommentsList();
    }

    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable UUID uuid) throws NoSuchIdException {
        return commentService.getCommentById(uuid);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void createComment(@RequestBody CommentDto commentDto) throws NoSuchIdException {
        commentService.createComment(commentDto.getIdNews(), commentDto);
    }

    @PatchMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@PathVariable UUID uuid, @RequestBody CommentDto commentDto) throws NoSuchIdException {
        commentService.updateComment(uuid, commentDto);
    }

    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable UUID uuid) throws NoSuchIdException {
        commentService.deleteComment(uuid);
    }

}
