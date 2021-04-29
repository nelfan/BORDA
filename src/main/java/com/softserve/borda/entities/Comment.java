package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @NotBlank
    private String text;

    @ToString.Exclude
    @ManyToOne
    private User user;

    @ToString.Exclude
    @ManyToOne
    private Ticket ticket;
}
