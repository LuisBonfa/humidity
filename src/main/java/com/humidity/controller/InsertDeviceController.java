package com.humidity.controller;

import com.humidity.controller.bean.InsertDeviceData;
import com.humidity.domain.DeviceManagement;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class InsertDeviceController extends SimpleController {
    public InsertDeviceController() {
        super(500, "245fb25a-ccf3-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        InsertDeviceData deviceData = Json.decodeValue(routingContext.getBodyAsString(), InsertDeviceData.class);
        response.setStatusCode(200).end(Json.encode(DeviceManagement.insertDevice(u, deviceData.getModelId(), deviceData.getAreaId(), deviceData.getName())));
    }
}
