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

public class ListHumiditiesByDeviceOrAreaAndDate extends SimpleController {

    public ListHumiditiesByDeviceOrAreaAndDate() {
        super(500, "9c6c3cd8-cdba-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        String begin = request.getParam("begin");
        String end = request.getParam("end");
        UUID deviceOrAreaId = UUID.fromString(request.getParam("deviceOrAreaId"));

        List<Humidity> humidities;

        try {
            humidities = HumidityManagement.loadHumiditiesByAreaAndDate(u, deviceOrAreaId, begin, end);
        } catch (Exception e) {
            humidities = HumidityManagement.loadHumiditiesByDeviceAndDate(u, deviceOrAreaId, begin, end);
        }

        response.setStatusCode(200).end(Json.encode(humidities));
    }
}
