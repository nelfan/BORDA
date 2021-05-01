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
    @Column(name = "ubr_id")
    private Long id;

    @ToString.Exclude
    @ManyToOne
    private User user;

    @ToString.Exclude
    @ManyToOne
    private Board board;

    @ToString.Exclude
    @ManyToMany
    private List<Role> roles = new ArrayList<>();

}
