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

import static com.example.kadastr.util.StringsStorage.*;

//todo comments, Readme, refactoring(strings..., sout, import)

//controller that dispatch requests of comment entity
@RestController
@RequestMapping(COMMENTS_CONTROLLER_URL)
public class CommentsController extends AbstractController {

    private static final String COMMENT_CREATED = "Comment was successfully created";
    private static final String COMMENT_WITH_ID = "Comment with uuid = ";
    private static final String COMMENT_WAS_UPDATED = " was successfully updated";
    private static final String COMMENT_WAS_DELETED = " was successfully deleted";

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
        return constructAnswer(COMMENT_CREATED, CREATED_STATUS);
    }

    @PatchMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson updateComment(@PathVariable UUID uuid, @Valid @RequestBody CommentDto commentDto, BindingResult bindingResult) throws NoSuchIdException, InvalidInputDataException, IllegalControlException {
        bindingResultCheck(bindingResult);
        commentService.updateComment(uuid, commentDto);
        return constructAnswer(COMMENT_WITH_ID + uuid + COMMENT_WAS_UPDATED, UPDATED_STATUS);
    }

    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson deleteComment(@PathVariable UUID uuid) throws NoSuchIdException, IllegalControlException {
        commentService.deleteComment(uuid);
        return constructAnswer(COMMENT_WITH_ID + uuid + COMMENT_WAS_DELETED, DELETED_STATUS);
    }

}
