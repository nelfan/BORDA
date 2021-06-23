package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    @JsonBackReference
    private Board board;

    @Column(name = "board_id")
    private Long boardId;

    @ToString.Exclude
    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private List<Ticket> tickets;
}