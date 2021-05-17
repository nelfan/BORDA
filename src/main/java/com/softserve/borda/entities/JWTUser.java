package com.softserve.borda.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "jwtuser")
public class JWTUser {
    @Id
    private String token;
    @OneToOne()
    @JoinColumn(name = "users_id")
    private User user;
}
