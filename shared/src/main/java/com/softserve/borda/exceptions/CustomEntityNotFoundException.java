package com.softserve.borda.exceptions;

public class CustomEntityNotFoundException extends RuntimeException {
    public CustomEntityNotFoundException(){
        super("Entity not found");
    }
    public CustomEntityNotFoundException(String message){
        super("Entity not found: \n" + message);
    }
    public CustomEntityNotFoundException(Class<?> clazz){
        super("Unable to find " + clazz.getSimpleName().toLowerCase());
    }
}
