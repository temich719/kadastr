package com.example.kadastr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private String text;
    private UUID insertedById;
    private UUID idNews;

}
