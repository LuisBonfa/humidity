package com.humidity.domain.error;

import com.ultraschemer.microweb.error.StandardException;

public class ListHumiditiesAcquiringException extends StandardException {

    public ListHumiditiesAcquiringException(String message) {
        super("e6bb03e8-cb88-11e9-a32f-2a2ae2dbcce4", 500, message);
    }
}
