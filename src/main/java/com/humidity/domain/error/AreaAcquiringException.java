package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

public class AreaAcquiringException extends StandardException {

    public AreaAcquiringException(String message, Throwable cause) {
        super("9504c120-cb8c-11e9-a32f-2a2ae2dbcce4", 500, message, cause);
    }
}
