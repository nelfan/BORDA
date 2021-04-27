package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
@Entity
public class UserBoardRelation {
    @Id
    private Long id;

    @ToString.Exclude
    @ManyToOne
    private User user;

    @ToString.Exclude
    @ManyToOne
    private Board board;

    @ToString.Exclude
    @ManyToMany
    private List<Role> roles;

}
