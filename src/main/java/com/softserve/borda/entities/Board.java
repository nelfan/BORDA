package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<BoardList> boardLists = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<UserBoardRelation> userBoardRelations = new ArrayList<>();

}
