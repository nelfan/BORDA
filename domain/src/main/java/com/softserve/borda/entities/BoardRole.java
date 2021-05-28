package com.softserve.borda.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "board_roles")
public class BoardRole {

    public enum BoardRoles {
        OWNER,
        COLLABORATOR,
        GUEST
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @NotBlank
    private String name;

    public BoardRole() {
    }

    public BoardRole(@NotBlank String name) {
        this.name = name;
    }

}
