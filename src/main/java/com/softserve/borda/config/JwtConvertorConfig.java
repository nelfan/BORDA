package com.softserve.borda.config;

import com.softserve.borda.config.jwt.JwtConvertor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConvertorConfig {
    @Bean
    public JwtConvertor jwtConvertor() {
        JwtConvertor jwtConvertor = new JwtConvertor();
        return jwtConvertor;
    }
}
