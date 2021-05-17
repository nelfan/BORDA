package com.softserve.borda.exceptions;

import lombok.extern.java.Log;

@Log
public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(){
        super("Entity not found");
        log.warning("Entity not found");
    }
    public CustomEntityNotFoundException(String message){
        super("Entity not found: \n" + message);
        log.warning("Entity not found: \n" + message);
    }
    public CustomEntityNotFoundException(Class<?> clazz){
        super("Unable to find " + clazz.getSimpleName().toLowerCase());
        log.warning("Unable to find " + clazz.getSimpleName().toLowerCase());
    }
}
