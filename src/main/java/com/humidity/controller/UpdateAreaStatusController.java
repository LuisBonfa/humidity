package com.humidity.controller;

import com.humidity.domain.AreaManagement;
import com.humidity.entity.Area;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public class UpdateAreaStatusController extends SimpleController {
    public UpdateAreaStatusController() {
        super(500, "e01a795a-cce7-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        JsonObject data = new JsonObject(routingContext.getBodyAsString());
        String action = data.getString("action");
        UUID areaId = UUID.fromString(request.getParam("areaId"));

        Area area = new Area();
        if(action.equals("enable"))
            area = AreaManagement.enableArea(u, areaId);
        else if(action.equals("disable"))
            area = AreaManagement.disableArea(u, areaId);

        response.setStatusCode(200).end(Json.encode(area));
    }
}
