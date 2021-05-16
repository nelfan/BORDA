package com.softserve.borda.config;

import com.softserve.borda.config.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtProviderConfig {
    @Bean
    public JwtProvider jwtConvertor() {
        JwtProvider jwtProvider = new JwtProvider();
        return jwtProvider;
    }
}
