package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

import javax.persistence.PersistenceException;

public class InsertHumidityException extends StandardException {

    public InsertHumidityException(String message, Throwable cause) {
        super("6abfb260-cda5-11e9-a32f-2a2ae2dbcce4", 500, message, cause);
    }
}
