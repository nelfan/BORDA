package com.softserve.borda.dto;

import lombok.Data;

@Data
public class TagDTO {

    private Long id;
    private String text;
    private String color;
    private Long boardId;
}
