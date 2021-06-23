package com.softserve.borda.config;

import com.softserve.borda.dto.BoardColumnDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.List;

@Configuration
public class SinksConfiguration {

    @Bean
    public Sinks.Many<Object> sink(){
        return Sinks.many().replay().latest();
    }

    @Bean
    public Sinks.Many<List<BoardColumnDTO>> sinkColumns(){
        return Sinks.many().replay().latest();
    }
}
