package com.softserve.borda.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    @Column(unique=true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;


    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] avatar;

    @ToString.Exclude
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    List<UserBoardRelation> userBoardRelations = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    List<Comment> comments = new ArrayList<>();
}
