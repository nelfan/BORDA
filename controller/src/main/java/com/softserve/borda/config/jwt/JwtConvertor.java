package com.softserve.borda.config.jwt;

import com.softserve.borda.entities.JWTUser;
import com.softserve.borda.entities.User;
import com.softserve.borda.exceptions.CustomEntityNotFoundException;
import com.softserve.borda.repositories.JWTUserRepository;
import org.springframework.stereotype.Service;

@Service
public class JwtConvertor {

    private final JwtProvider jwtProvider;

    private final JWTUserRepository jwtUserRepository;

    public JwtConvertor(com.softserve.borda.config.jwt.JwtProvider jwtProvider, JWTUserRepository jwtUserRepository) {
        this.jwtProvider = jwtProvider;
        this.jwtUserRepository = jwtUserRepository;
    }

    public String saveUser(User user){
        jwtUserRepository.save(new JWTUser(jwtProvider.generateToken(user.getUsername()),user));
        return jwtProvider.generateToken(user.getUsername());
    }

    public User getUserByJWT(String jwt){
        return jwtUserRepository.findById(jwt.substring(7)).orElseThrow(
                () -> new CustomEntityNotFoundException(User.class)).getUser();
    }
}
