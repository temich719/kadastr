package com.example.kadastr.dto;

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

    private String title;
    private String text;
    private UUID insertedById;
    private UUID updatedById;
    private List<CommentDto> comments = new ArrayList<>();

}
