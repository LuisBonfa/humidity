package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

import javax.persistence.PersistenceException;

public class EnableAreaException extends StandardException {

    public EnableAreaException(String message, Throwable cause) {
        super("81747914-cb8e-11e9-a32f-2a2ae2dbcce4", 500, message, cause);
    }
}
