package com.softserve.borda.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "tags")
public class Tag {

    public enum Color {
        BLUE,
        RED,
        YELLOW,
        GREEN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @NotBlank
    private String text;

    @NotBlank
    @Enumerated
    private Color color;
}