package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class UserBoardRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubr_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Board board;

    @ToString.Exclude
    @ManyToMany
    @JoinColumn(name = "role_id")
    private List<Role> roles = new ArrayList<>();

}
