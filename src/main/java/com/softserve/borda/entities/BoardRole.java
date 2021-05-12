package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

    @ToString.Exclude
    @ManyToMany
    @JoinColumn(name = "permission_id")
      
    private List<Permission> permissions = new ArrayList<>();

    public BoardRole() {
    }

    public BoardRole(@NotBlank String name) {
        this.name = name;
    }

}
