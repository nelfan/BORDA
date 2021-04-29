package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @NotBlank
    private String name;

    @ToString.Exclude
    @ManyToMany
    private List<Permission> permissions;

    @ToString.Exclude
    @ManyToMany
    private List<UserBoardRelation> userBoardRelations;

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
