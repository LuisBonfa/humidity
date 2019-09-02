package com.humidity.controller;

import com.humidity.controller.bean.InsertAreaData;
import com.humidity.domain.AreaManagement;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;


public class InsertAreaController extends SimpleController {
    public InsertAreaController() {
        super(500, "2e9f7b04-cc0f-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        InsertAreaData areaData = Json.decodeValue(routingContext.getBodyAsString(), InsertAreaData.class);

        response.setStatusCode(200).end(Json.encode(AreaManagement.insertArea(u, areaData.getName(), areaData.getDescription())));
    }
}
