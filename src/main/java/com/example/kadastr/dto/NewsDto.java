package com.example.kadastr.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.kadastr.util.StringsStorage.NEWS_TEXT_VALIDATOR_MESSAGE;
import static com.example.kadastr.util.StringsStorage.NEWS_TITLE_VALIDATOR_MESSAGE;

//Data transfer object of News entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {

    @Size(min = 1, max = 150, message = NEWS_TITLE_VALIDATOR_MESSAGE)
    private String title;
    @Size(min = 1, max = 2000, message = NEWS_TEXT_VALIDATOR_MESSAGE)
    private String text;
    private UUID insertedById;
    private UUID updatedById;
    private List<CommentDto> comments = new ArrayList<>();

}
