package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JoinColumn(name = "board_list_id", nullable = false)
    @JsonBackReference
    private BoardList boardList;

    @ToString.Exclude
    @OneToMany(mappedBy = "ticket",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany
    @JoinColumn(name = "tag_id")
    @JsonManagedReference
    private List<Tag> tags = new ArrayList<>();

}
