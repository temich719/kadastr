package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.dto.CommentDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.InvalidInputDataException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//todo comments, tests, logger(aop logger), exceptionHandler, JavaDoc, Readme, Caching

@RestController
@RequestMapping("/api/comments")
public class CommentsController extends AbstractController {

    private final CommentService commentService;

    @Autowired
    public CommentsController(ObjectProvider<AnswerMessageJson> answerMessageJson, CommentService commentService) {
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
    public AnswerMessageJson createComment(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult) throws NoSuchIdException, InvalidInputDataException, AuthException {
        bindingResultCheck(bindingResult);
        commentService.createComment(commentDto.getIdNews(), commentDto);
        return constructAnswer("Comment was successfully created", "CREATED");
    }

    @PatchMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson updateComment(@PathVariable UUID uuid, @Valid @RequestBody CommentDto commentDto, BindingResult bindingResult) throws NoSuchIdException, InvalidInputDataException, IllegalControlException {
        bindingResultCheck(bindingResult);
        commentService.updateComment(uuid, commentDto);
        return constructAnswer("Comment with uuid = " + uuid + " was successfully updated", "UPDATED");
    }

    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson deleteComment(@PathVariable UUID uuid) throws NoSuchIdException, IllegalControlException {
        commentService.deleteComment(uuid);
        return constructAnswer("Comment with uuid = " + uuid + " was successfully deleted", "DELETED");
    }

}
