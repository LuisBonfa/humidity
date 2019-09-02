package com.humidity.controller;

import com.humidity.domain.HumidityManagement;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

public class ListHumiditiesController extends SimpleController {
    public ListHumiditiesController() {
        super(500, "b1d2c056-cda6-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        String begin = request.getParam("begin");
        String end = request.getParam("end");

        if (Optional.ofNullable(begin).isPresent() && Optional.ofNullable(begin).isPresent())
            response.setStatusCode(200).end(Json.encode(HumidityManagement.loadHumiditiesByDate(u, begin, end)));
        else
            response.setStatusCode(200).end(Json.encode(HumidityManagement.loadHumidities(u)));
    }
}
