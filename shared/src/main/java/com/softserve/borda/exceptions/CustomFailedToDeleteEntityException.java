package com.softserve.borda.exceptions;

import lombok.extern.java.Log;

@Log
public class CustomFailedToDeleteEntityException extends RuntimeException {
    public CustomFailedToDeleteEntityException() {
        super("Unable to delete");
        log.warning("Unable to delete");
    }

    public CustomFailedToDeleteEntityException(String message) {
        super("Unable to delete: \n" + message);
        log.warning("Unable to delete: \n" + message);
    }

    public CustomFailedToDeleteEntityException(Class<?> clazz) {
        super("Unable to delete " + clazz.getSimpleName().toLowerCase());
        log.warning("Unable to delete " + clazz.getSimpleName().toLowerCase());
    }
}
