package com.humidity.controller;

import com.humidity.domain.DeviceManagement;
import com.humidity.entity.Device;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public class UpdateDeviceController extends SimpleController {
    public UpdateDeviceController() {
        super(500, "3cd52b76-ccf3-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        JsonObject deviceData = new JsonObject(routingContext.getBodyAsString());
        String action = deviceData.getString("action");
        UUID areaId = deviceData.getString("areaId") != null ? UUID.fromString(deviceData.getString("areaId")) : null;
        UUID deviceId = UUID.fromString(request.getParam("deviceId"));

        Device device;

        if (action.equals("enable")) {
            device = DeviceManagement.enableDevice(u, deviceId);
        } else if (action.equals("disable")) {
            device = DeviceManagement.disableDevice(u, deviceId);
        } else{
            device = DeviceManagement.linkDeviceToArea(u, deviceId, areaId);
        }

        response.setStatusCode(200).end(Json.encode(device));
    }
}
