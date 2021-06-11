package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    @JsonBackReference
    private Board board;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_board_role_id", insertable = false, updatable = false)
    @JsonManagedReference
    private UserBoardRole userBoardRole;

    @Column(name = "user_board_role_id")
    private Long userBoardRoleId;

}