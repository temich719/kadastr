package com.example.kadastr.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @Size(min = 1, max = 300, message = "Comment must be lower than 300 symbols and not empty!")
    private String text;
    private UUID insertedById;
    private UUID idNews;

}
