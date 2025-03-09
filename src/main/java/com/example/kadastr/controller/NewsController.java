package com.example.kadastr.controller;

import com.example.kadastr.dto.AnswerMessageJson;
import com.example.kadastr.dto.NewsDto;
import com.example.kadastr.exception.AuthException;
import com.example.kadastr.exception.IllegalControlException;
import com.example.kadastr.exception.InvalidInputDataException;
import com.example.kadastr.exception.NoSuchIdException;
import com.example.kadastr.service.NewsService;
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

@RestController
@RequestMapping(NEWS_CONTROLLER_URL)
public class NewsController extends AbstractController {

    private static final String ZERO = "0";
    private static final String FIVE = "5";
    private static final String NEWS_WAS_CREATED = "News was successfully created";
    private static final String NEWS_WITH_ID = "News with uuid = ";
    private static final String WAS_UPDATED = " was updated";
    private static final String WAS_DELETED = " was deleted";

    private final NewsService newsService;

    @Autowired
    public NewsController(ObjectProvider<AnswerMessageJson> answerMessageJson, NewsService newsService) {
        super(answerMessageJson);
        this.newsService = newsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> getNewsList(@RequestParam(defaultValue = ZERO) int page, @RequestParam(defaultValue = FIVE) int size) {
        return newsService.getNewsList(page, size);
    }

    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NewsDto getNewsById(@PathVariable UUID uuid,
                               @RequestParam(defaultValue = ZERO) int page,
                               @RequestParam(defaultValue = FIVE) int size) throws NoSuchIdException {
        return newsService.getNewsById(uuid, page, size);
    }

    @GetMapping(value = "/{text}/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> findNewsByTextAndTitle(@PathVariable String text, @PathVariable String title) {
        return newsService.findNewsByTextAndTitle(text, title);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson createNews(@Valid @RequestBody NewsDto newsDto, BindingResult bindingResult) throws InvalidInputDataException, AuthException {
        bindingResultCheck(bindingResult);
        newsService.createNews(newsDto);
        return constructAnswer(NEWS_WAS_CREATED, CREATED_STATUS);
    }

    @PatchMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson updateNewsById(@PathVariable UUID uuid, @Valid @RequestBody NewsDto newsDto, BindingResult bindingResult) throws NoSuchIdException, InvalidInputDataException, IllegalControlException, AuthException {
        bindingResultCheck(bindingResult);
        newsService.updateNews(uuid, newsDto);
        return constructAnswer(NEWS_WITH_ID + uuid + WAS_UPDATED, UPDATED_STATUS);
    }

    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson deleteNews(@PathVariable UUID uuid) throws NoSuchIdException, IllegalControlException {
        newsService.deleteNews(uuid);
        return constructAnswer(NEWS_WITH_ID + uuid + WAS_DELETED, DELETED_STATUS);
    }
}
