package com.softserve.borda.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "user_board_roles")
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

    @OneToMany(mappedBy = "userBoardRole")
    private List<UserBoardRelation> userBoardRelations = new ArrayList<>();

    @Column(name = "user_board_relation_id")
    private Long userBoardRelationId;

    public UserBoardRole() {
    }

    public UserBoardRole(@NotBlank String name) {
        this.name = name;
    }



}
