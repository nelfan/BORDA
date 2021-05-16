package com.softserve.borda.config.jwt;

import com.softserve.borda.entities.JWTUser;
import com.softserve.borda.entities.User;
import com.softserve.borda.repositories.JWTUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class JwtConvertor {

    private JwtProvider jwtProvider;

    @Autowired
    JWTUserRepository jwtUserRepository;

    public JwtConvertor(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    public String saveUser(User user){
        jwtUserRepository.save(new JWTUser(jwtProvider.generateToken(user.getUsername()),user));
        return jwtProvider.generateToken(user.getUsername());
    }

    public User getUserByJWT(String jwt){
        return jwtUserRepository.findById(jwt).orElseThrow().getUser();
    }
}
