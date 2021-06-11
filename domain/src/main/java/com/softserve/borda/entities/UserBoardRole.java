package com.softserve.borda.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "user_board_roles")
public class UserBoardRole {

    public enum UserBoardRoles {
        OWNER(1L),
        COLLABORATOR(2L),
        VIEWER(3L);

        private final Long id;

        UserBoardRoles(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_board_role_id")
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "userBoardRole")
    @JsonBackReference
    @ToString.Exclude
    private List<UserBoardRelation> userBoardRelations = new ArrayList<>();

    public UserBoardRole() {
    }

    public UserBoardRole(@NotBlank String name) {
        this.name = name;
    }
}
