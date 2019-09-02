package com.humidity.controller;

import com.humidity.domain.HumidityManagement;
import com.humidity.entity.Humidity;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.UUID;

public class ListHumiditiesByIdController extends SimpleController {
    public ListHumiditiesByIdController() {
        super(500, "c96a2794-cdac-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        UUID deviceOrAreaId = UUID.fromString(request.getParam("deviceOrAreaId"));

        List<Humidity> humidities;
        try {
            humidities = HumidityManagement.loadHumiditiesByArea(u, deviceOrAreaId);
        } catch (Exception e) {
            humidities = HumidityManagement.loadHumiditiesByDevice(u, deviceOrAreaId);
        }

        response.setStatusCode(200).end(Json.encode(humidities));
    }
}
