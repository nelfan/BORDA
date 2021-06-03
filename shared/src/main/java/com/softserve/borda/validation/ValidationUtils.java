package com.softserve.borda.validation;

import lombok.extern.java.Log;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Log
public class ValidationUtils {

    public static final String REG_EXP_USERNAME = "^(?!.*[_]{2})[a-zA-Z0-9_]+(?<![_.])$";
    public static final String REG_EXP_LAST_NAME = "^[A-Z][a-z]*([-][A-Z][a-z]*)?$";
    public static final String REG_EXP_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$";
    public static final String REG_EXP_BOARD_NAME = "^[\\s~`!@#$%^&*()_+=[\\\\]\\\\{}|;':\",.\\\\/<>?a-zA-Z0-9-]+$";

    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);
        Map<String, String> errorsMap = bindingResult.getFieldErrors().stream().collect(collector);

        return errorsMap;
    }

    public static void logErrors(Map<String, String> errorsMap) {
        for (Map.Entry<String, String> err : errorsMap.entrySet()) {
            log.severe(err.getKey() + ": " + err.getValue());
        }
    }

}