package com.softserve.borda.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "roles")
public class Role {

    public enum Roles {
        ADMIN,
        USER
    }

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    public Role() {
    }

    public Role(@NotBlank String name) {
        this.name = name;
    }
}
