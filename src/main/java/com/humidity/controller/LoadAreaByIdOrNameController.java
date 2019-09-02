package com.humidity.controller;

import com.humidity.domain.AreaManagement;
import com.humidity.entity.Area;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class LoadAreaByIdOrNameController extends SimpleController {
    public LoadAreaByIdOrNameController() {
        super(500, "e57faf8c-cce7-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        String areaIdOrName = request.getParam("areaIdOrName");
        Area area;
        try {
            area = AreaManagement.loadAreaById(u, areaIdOrName);
        } catch (Exception e) {
            area = AreaManagement.loadAreaByName(u, areaIdOrName);
        }

        response.setStatusCode(200).end(Json.encode(area));
    }
}
