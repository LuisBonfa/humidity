package com.humidity.controller;

import com.humidity.domain.DeviceManagement;
import com.humidity.entity.Device;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

public class LoadDeviceByIdOrNameController extends SimpleController {
    public LoadDeviceByIdOrNameController() {
        super(500, "27e7f572-ccf3-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        String deviceIdOrName = request.getParam("deviceIdOrName");

        Device device;
        try {
            device = DeviceManagement.loadDeviceById(u, deviceIdOrName);
        } catch (Exception e) {
            device = DeviceManagement.loadDeviceByName(u, deviceIdOrName);
        }

        response.setStatusCode(200).end(Json.encode(device));
    }
}
