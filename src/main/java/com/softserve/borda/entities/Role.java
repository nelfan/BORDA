package com.softserve.borda.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "roles")
public class Role implements GrantedAuthority {

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

    @Override
    public String getAuthority() {
        return name;
    }
}
