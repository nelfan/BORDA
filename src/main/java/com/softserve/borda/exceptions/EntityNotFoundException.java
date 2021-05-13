package com.softserve.borda.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(){
        super("Entity not found");
        log.warn("Entity not found");
    }
    public EntityNotFoundException(String message){
        super("Entity not found: \n" + message);
        log.warn("Entity not found: \n" + message);
    }
    public EntityNotFoundException(Class<?> clazz){
        super("Unable to delete " + clazz.getSimpleName().toLowerCase());
        log.warn("Unable to delete {}", clazz.getSimpleName().toLowerCase());
    }
}
