package com.humidity.controller;

import com.humidity.domain.HumidityManagement;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ListHumiditiesController extends SimpleController {
    public ListHumiditiesController() {
        super(500, "b1d2c056-cda6-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        JsonObject allHumidities = HumidityManagement.loadHumidities(u);
        response.setStatusCode(200).end(Json.encode(allHumidities));
    }
}
