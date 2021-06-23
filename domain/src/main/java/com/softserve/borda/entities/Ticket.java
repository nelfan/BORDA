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

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "tickets_members",
            joinColumns = { @JoinColumn(name = "ticket_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private List<User> members = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "tickets_tags",
            joinColumns = { @JoinColumn(name = "ticket_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    private List<Tag> tags = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_column_id", insertable = false, updatable = false)
    private BoardColumn boardColumn;

    @Column(name = "board_column_id")
    private Long boardColumnId;

}