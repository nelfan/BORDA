package com.softserve.borda.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(){
        super("Entity not found");
        log.warn("Entity not found");
    }
    public CustomEntityNotFoundException(String message){
        super("Entity not found: \n" + message);
        log.warn("Entity not found: \n" + message);
    }
    public CustomEntityNotFoundException(Class<?> clazz){
        super("Unable to delete " + clazz.getSimpleName().toLowerCase());
        log.warn("Unable to delete {}", clazz.getSimpleName().toLowerCase());
    }
}
