package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

public class HumidityAcquiringException extends StandardException {

    public HumidityAcquiringException(String message, Throwable cause) {
        super("ff91ea26-cb88-11e9-a32f-2a2ae2dbcce4", 500, message, cause);
    }
}
