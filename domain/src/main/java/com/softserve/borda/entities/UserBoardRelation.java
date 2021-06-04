package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
public class UserBoardRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubr_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne
    @JsonBackReference
    private User user;

    @ToString.Exclude
    @ManyToOne
    @JsonBackReference
    private Board board;

    @ToString.Exclude
    @ManyToOne
    private UserBoardRole userBoardRole;

}