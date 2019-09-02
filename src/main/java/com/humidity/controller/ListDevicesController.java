package com.humidity.controller;

import com.humidity.domain.DeviceManagement;
import com.humidity.entity.Device;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Optional;

public class ListDevicesController extends SimpleController {
    public ListDevicesController() {
        super(500, "f0dd6008-ccf2-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        String status = request.getParam("status");
        String model = request.getParam("model");

        List<Device> devices;

        if (status != null && !status.isEmpty()) {
            devices = DeviceManagement.loadDevicesByStatus(u, status);
        } else if (model != null && model.isEmpty()) {
            devices = DeviceManagement.loadDevicesByModel(u, model);
        } else {
            devices = DeviceManagement.loadDevices(u);
        }

        response.setStatusCode(200).end(Json.encode(devices));

    }
}
