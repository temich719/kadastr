package com.example.kadastr.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {

    @Size(min = 1, max = 150, message = "Title must be lower than 150 symbols and not empty!")
    private String title;
    @Size(min = 1, max = 2000, message = "Text must be lower than 2000 symbols and not empty!")
    private String text;
    private UUID insertedById;
    private UUID updatedById;
    private List<CommentDto> comments = new ArrayList<>();

}
