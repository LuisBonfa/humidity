package com.humidity.controller.bean;

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import java.math.BigInteger;
import java.util.UUID;

public class InsertHumidityData {

    @NotNull
    @NotEmpty
    private UUID areaId;

    @NotNull
    @NotEmpty
    private UUID deviceId;

    @NotNull
    @NotEmpty
    private BigInteger value;

    public UUID getAreaId() {
        return areaId;
    }

    public void setAreaId(UUID areaId) {
        this.areaId = areaId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
