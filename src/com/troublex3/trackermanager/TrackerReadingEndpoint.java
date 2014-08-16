package com.troublex3.trackermanager;

import com.google.api.server.spi.config.*;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;

/**
 * Created by rodtoll on 7/24/14.
 */
@Api(name="readings", version="v1", description = "Service for Tracker Readings")
public class TrackerReadingEndpoint {

    @ApiMethod(
            name = "readings.list",
            path = "node/{nodeId}/readings",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public List<TrackerReading> list(User user, HttpServletRequest request, @Named("nodeId") String nodeId) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerNode node = TrackerStore.getNode(nodeId);
        if(node != null) {
            return new ArrayList<TrackerReading>(TrackerStore.getReadings(node));
        } else {

            return new ArrayList<TrackerReading>();
        }
    }



    @ApiMethod(
            name = "readings.listbyaddress",
            path = "node/{nodeId}/readings/{address}/list",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public List<TrackerReading> listByAddress(User user, HttpServletRequest request, @Named("nodeId") String nodeId, @Named("address") String address) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerNode node = TrackerStore.getNode(nodeId);
        if(node != null) {
            return TrackerStore.getReadings(node,address);
        } else {
            return new ArrayList<TrackerReading>();
        }
    }

    @ApiMethod(
            name = "readings.getlastreading",
            path = "node/{nodeId}/readings/{address}/last",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public TrackerReading getLastReading(User user, HttpServletRequest request, @Named("nodeId") String nodeId, @Named("address") String address) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerNode node = TrackerStore.getNode(nodeId);
        if(node != null) {
            return node.getLastReading();
        }
        return null;
    }

    @ApiMethod(
            name = "readings.add",
            path = "node/{nodeId}/readings",
            httpMethod = ApiMethod.HttpMethod.POST,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public void add(
            User user,
            HttpServletRequest request,
            @Named("nodeId")
            String nodeId, @Named("address") String address,
            @Named("signalStrength") Double signalStrength) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerNode node = TrackerStore.getNode(nodeId);
        if(node != null) {
            Date timeStamp = new Date();
            node.setLastHeartbeat(timeStamp);
            TrackerReading reading = new TrackerReading();
            reading.setAddress(address);
            reading.setSignalStrength(signalStrength);
            reading.setTimeStamp(timeStamp);
            TrackerStore.addReading(node, reading);
            node.setLastReading(reading);
            TrackerStore.updateNode(node);
        }
    }

/*    @ApiMethod(
            name = "readings.remove",
            path = "node/{nodeId}/readings/{readingId}",
            httpMethod = ApiMethod.HttpMethod.DELETE,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public void delete(
            User user,
            HttpServletRequest request,
            @Named("nodeId") String nodeId,
            @Named("readingId") String readingId)  throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerNode node = TrackerStore.getNode(nodeId);
        if(node != null) {
            node.setLastHeartbeat(new Date());
            UUID id = UUID.fromString(readingId);
            TrackerStore.deleteReading(node, id);
            TrackerStore.updateNode(node);
        }
    } */

    @ApiMethod(
            name = "readings.flush",
            path = "node/flushall",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public void flush(User user, HttpServletRequest request) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        TrackerStore.flush();
    }
}
