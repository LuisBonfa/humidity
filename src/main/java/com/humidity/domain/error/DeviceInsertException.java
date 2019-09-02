package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

import javax.persistence.PersistenceException;

public class DeviceInsertException extends StandardException {

    public DeviceInsertException(String message, Throwable cause) {
        super("16988e76-cc17-11e9-a32f-2a2ae2dbcce4", 500, message, cause);
    }
}
