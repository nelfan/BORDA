package com.softserve.borda.entities;


import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long id;

    @NotBlank
    private String name;

    @ToString.Exclude
    @ManyToMany
    @JoinColumn(name = "role_id")
    private List<Role> roles = new ArrayList<>();
}
