package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @NotBlank(message = "The board name cannot be empty")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BoardColumn> boardColumns = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "board",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Tag> tags = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "board",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<UserBoardRelation> userBoardRelations = new ArrayList<>();

}
