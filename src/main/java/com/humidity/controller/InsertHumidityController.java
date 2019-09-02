package com.humidity.controller;

import com.humidity.controller.bean.InsertHumidityData;
import com.humidity.domain.HumidityManagement;
import com.humidity.entity.Humidity;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class InsertHumidityController extends SimpleController {

    public InsertHumidityController() {
        super(500, "6d04a296-cda6-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        InsertHumidityData humidityData =  Json.decodeValue(routingContext.getBodyAsString(), InsertHumidityData.class);
        response.setStatusCode(200).end(Json.encode(HumidityManagement.insertHumidity(u,humidityData.getAreaId(),
                humidityData.getDeviceId(), humidityData.getValue())));

    }
}
