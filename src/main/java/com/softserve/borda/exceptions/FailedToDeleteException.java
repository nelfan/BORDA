package com.softserve.borda.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FailedToDeleteException extends RuntimeException {

    public FailedToDeleteException(){
        super("Unable to delete");
        log.warn("Unable to delete");
    }
    public FailedToDeleteException(String message){
        super("Unable to delete: \n" + message);
        log.warn("Unable to delete: \n" + message);
    }
    public FailedToDeleteException(Class<?> clazz){
        super("Unable to delete " + clazz.getSimpleName().toLowerCase());
        log.warn("Unable to delete {}", clazz.getSimpleName().toLowerCase());
    }
}