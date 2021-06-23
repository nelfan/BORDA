package com.softserve.borda.entities;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "roles")
public class Role implements GrantedAuthority {

    public enum Roles {
        ROLE_ADMIN("ADMIN"),
        ROLE_USER("USER");

        private final String roleName;

        Roles(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }
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
