package com.softserve.borda.dto;

import com.softserve.borda.entities.Tag;
import lombok.Data;

@Data
public class TagDTO {

    private Long id;
    private String text;
    private Tag.Color color;
}
