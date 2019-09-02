package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

public class DeviceAcquiringException extends StandardException {

    public DeviceAcquiringException(String message) {
        super("ba4ececc-cc17-11e9-a32f-2a2ae2dbcce4", 500, message);
    }
}
