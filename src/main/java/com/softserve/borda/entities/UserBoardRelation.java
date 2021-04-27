package com.softserve.borda.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class UserBoardRelation {

    private Long id;
    private User user;
    private Board board;
    private List<Role> roles;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
