package com.troublex3.trackermanager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by rodtoll on 7/25/14.
 */
@Api(name="tracker", version="v1", description = "Service for Trackers")
public class TrackerNodeEndpoint {

    public static final String CurrentString = "current";

    @ApiMethod(
            name = "node.heartbeat",
            path = "node/{id}",
            httpMethod =  ApiMethod.HttpMethod.PUT,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public void heartbeat(User user, HttpServletRequest request, @Named("id") String id) throws UnauthorizedException, NotFoundException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        if(id.contentEquals(TrackerNodeEndpoint.CurrentString)) {
            id = request.getRemoteAddr();
        }
        Date timeToUse = new Date();
        if(!TrackerController.markNodeHeartbeatSeen(id, timeToUse)) {
            throw new NotFoundException("Specified node does not exist");
        }
        TrackerController.doPresenceCheckIfNeeded(timeToUse);
    }

    @ApiMethod(
            name = "node.getNode",
            path = "node/{id}",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public TrackerNode getNode(User user, HttpServletRequest request, @Named("id") String id) throws UnauthorizedException, NotFoundException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        if(id.contentEquals(TrackerNodeEndpoint.CurrentString)) {
            id = request.getRemoteAddr();
        }
        return TrackerController.getNode(id);
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
        return TrackerController.getNodes();
    }
}
