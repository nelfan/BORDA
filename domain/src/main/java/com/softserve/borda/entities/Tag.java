package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @NotBlank
    private String text;

    @NotBlank
    private String color;

    @ToString.Exclude
    @ManyToOne
    @JsonBackReference
    private Board board;

    @Column
    private Long boardId;
}