package com.softserve.borda.config.jwt;

import com.softserve.borda.entities.JWTUser;
import com.softserve.borda.entities.User;
import com.softserve.borda.repositories.JWTUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JwtConvertor {

    private final JwtProvider jwtProvider;

    private JWTUserRepository jwtUserRepository;


    public JwtConvertor(JwtProvider jwtProvider, JWTUserRepository jwtUserRepository) {
        this.jwtProvider = jwtProvider;
        this.jwtUserRepository = jwtUserRepository;
    }


    public String saveUser(User user){
        jwtUserRepository.save(new JWTUser(jwtProvider.generateToken(user.getUsername()),user));
        return jwtProvider.generateToken(user.getUsername());
    }

    public User getUserByJWT(String jwt){
        return jwtUserRepository.findById(jwt).orElseThrow().getUser();
    }
}
