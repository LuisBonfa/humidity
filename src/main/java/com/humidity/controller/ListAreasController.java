package com.humidity.controller;

import com.humidity.domain.AreaManagement;
import com.humidity.entity.Area;
import com.ultraschemer.microweb.entity.User;
import com.ultraschemer.microweb.vertx.SimpleController;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class ListAreasController extends SimpleController {
    public ListAreasController() {
        super(500, "0e2c2f86-cc12-11e9-a32f-2a2ae2dbcce4");
    }

    @Override
    public void executeEvaluation(RoutingContext routingContext, HttpServerResponse response) throws Throwable {
        User u = routingContext.get("user");
        HttpServerRequest request = routingContext.request();
        String status = request.getParam("status");
        List<Area> areas;
        if (status == null || status.isEmpty()) {
            areas = AreaManagement.loadAreas(u);
        } else {
            areas = AreaManagement.loadAreasByStatus(u, status);
        }
        response.setStatusCode(200).end(Json.encode(areas));
    }
}
