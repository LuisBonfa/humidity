package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

import java.text.ParseException;

public class HumidityDateException extends StandardException {

    public HumidityDateException(String message, Throwable cause) {
        super("108e2d22-cdb8-11e9-a32f-2a2ae2dbcce4", 500, message, cause);
    }
}
