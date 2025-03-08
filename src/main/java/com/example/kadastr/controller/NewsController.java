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

@RestController
@RequestMapping("/api/news")
public class NewsController extends AbstractController {

    private final NewsService newsService;

    @Autowired
    public NewsController(ObjectProvider<AnswerMessageJson> answerMessageJson, NewsService newsService) {
        super(answerMessageJson);
        this.newsService = newsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<NewsDto> getNewsList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return newsService.getNewsList(page, size);
    }

    @GetMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NewsDto getNewsById(@PathVariable UUID uuid,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) throws NoSuchIdException {
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
        return constructAnswer("News was successfully created", "CREATED");
    }

    @PatchMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson updateNewsById(@PathVariable UUID uuid, @Valid @RequestBody NewsDto newsDto, BindingResult bindingResult) throws NoSuchIdException, InvalidInputDataException, IllegalControlException, AuthException {
        bindingResultCheck(bindingResult);
        newsService.updateNews(uuid, newsDto);
        return constructAnswer("News with uuid = " + uuid + " was updated", "UPDATED");
    }

    @DeleteMapping(value = "/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public AnswerMessageJson deleteNews(@PathVariable UUID uuid) throws NoSuchIdException, IllegalControlException {
        newsService.deleteNews(uuid);
        return constructAnswer("News with uuid = " + uuid + " was deleted", "DELETED");
    }
}
