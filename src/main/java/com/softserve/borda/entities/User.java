package com.softserve.borda.entities;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] user_photo;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    List<UserBoardRelation> userBoardRelations;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    List<Comment> comments;

}
