package com.example.kadastr.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static com.example.kadastr.util.StringsStorage.COMMENT_TEXT_VALIDATOR_MESSAGE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @Size(min = 1, max = 300, message = COMMENT_TEXT_VALIDATOR_MESSAGE)
    private String text;
    private UUID insertedById;
    private UUID idNews;

}
