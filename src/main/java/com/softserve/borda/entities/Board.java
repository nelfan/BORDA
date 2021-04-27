package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<BoardList> boardLists;

    @ToString.Exclude
    @OneToMany(mappedBy = "board")
    private List<UserBoardRelation> userBoardRelations;

}
