package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;
    private String name;
    private String body;

    @ToString.Exclude
    @ManyToOne
    private BoardList boardList;

    @ToString.Exclude
    @OneToMany(mappedBy = "ticket",
            cascade = CascadeType.ALL)
    List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

}
