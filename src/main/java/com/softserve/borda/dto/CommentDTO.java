package com.softserve.borda.dto;

import lombok.Data;

@Data
public class CommentDTO {

    private Long id;
    private String text;
    private Long userId;
}
