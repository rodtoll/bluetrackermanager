package com.troublex3.trackermanager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by rodtoll on 7/25/14.
 */
@Api(name="node", version="v1", description = "Service for Tracker Node Management")
public class TrackerNodeEndpoint {
    @ApiMethod(
            name = "node.heartbeat",
            path = "node/{id}",
            httpMethod =  ApiMethod.HttpMethod.PUT,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public void heartbeat(User user, HttpServletRequest request, @Named("id") String id) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerNode node = TrackerStore.getNode(id);
        if(node != null) {
            node.setLastHeartbeat(new Date());
        }
    }

    @ApiMethod(
            name = "node.getNode",
            path = "node/{id}",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public TrackerNode getNode(User user, HttpServletRequest request, @Named("id") String id) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        return TrackerStore.getNode(id);
    }

    @ApiMethod(
            name = "node.list",
            path = "node",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public List<TrackerNode> nodeList(User user, HttpServletRequest request) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        return TrackerStore.getNodeList();
    }
}
