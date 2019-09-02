package com.humidity.controller;

import com.humidity.domain.ModelManagement;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class ListModelsController extends SimpleController {
    public ListModelsController() {
        super(500, "70488d66-cd9f-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        response.setStatusCode(200).end(Json.encode(ModelManagement.loadModels(u)));
    }
}
