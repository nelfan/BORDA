package com.softserve.borda.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "board_roles")
public class UserBoardRole {

    public enum UserBoardRoles {
        OWNER,
        COLLABORATOR,
        GUEST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_board_role_id")
    private Long id;

    @NotBlank
    private String name;

    public UserBoardRole() {
    }

    public UserBoardRole(@NotBlank String name) {
        this.name = name;
    }

}
