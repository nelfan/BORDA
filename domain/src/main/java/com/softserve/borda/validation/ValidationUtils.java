package com.softserve.borda.validation;

import lombok.extern.java.Log;

@Log
public class ValidationUtils {

    public static final String REG_EXP_USERNAME = "^(?!.*[_]{2})[a-zA-Z0-9_]+(?<![_.])$";
    public static final String REG_EXP_FIRST_NAME = "^[A-Z][a-z]*$";
    public static final String REG_EXP_LAST_NAME = "^[A-Z][a-z]*([-][A-Z][a-z]*)?$";
    public static final String REG_EXP_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]*$";
    public static final String REG_EXP_BOARD_NAME = "^[\\s~`!@#$%^&*()_+=[\\\\]\\\\{}|;':\",.\\\\/<>?a-zA-Z0-9-]+$";
    public static final String EMAIL = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
}