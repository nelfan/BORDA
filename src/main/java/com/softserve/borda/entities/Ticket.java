package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @NotBlank
    private String title;

    private String description;

    private Byte[] img;

    @ToString.Exclude
    @ManyToMany
    private List<User> members = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinColumn(name = "tag_id")
    private List<Tag> tags = new ArrayList<>();

}