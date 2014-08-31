package com.troublex3.trackermanager;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.utils.SystemProperty;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Date;
import java.util.Collection;
import java.util.SortedSet;
import java.net.URLDecoder;


/**
 * Created by rodtoll on 8/13/14.
 */
@Api(name="tracker", version="v1", description = "Service for Trackers")
public class TrackerDeviceEndpoint {
    @ApiMethod(
            name = "device.list",
            path = "device",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public List<TrackerDevice> deviceList(User user, HttpServletRequest request) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        return TrackerController.getDevices();
    }

    @ApiMethod(
            name = "device.getDevice",
            path = "device/{address}",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public TrackerDevice getDevice(User user, HttpServletRequest request, @Named("address") String address) throws UnauthorizedException, UnsupportedEncodingException {
        TrackerAuthentication.authenticateOrThrow(user, request);

        if(SystemProperty.environment.value() != SystemProperty.Environment.Value.Production) {
            address = URLDecoder.decode(address, "UTF-8");
        }

        return TrackerController.getDevice(address);
    }

    @ApiMethod(
            name = "device.reading.list",
            path = "device/{address}/reading",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public SortedSet<TrackerReading> getDeviceReadings(User user, HttpServletRequest request, @Named("address") String address) throws UnauthorizedException, UnsupportedEncodingException {
        TrackerAuthentication.authenticateOrThrow(user, request);

        if(SystemProperty.environment.value() != SystemProperty.Environment.Value.Production) {
            address = URLDecoder.decode(address, "UTF-8");
        }

        return TrackerController.getDeviceReadings(address);
    }

    @ApiMethod(
            name = "device.reading.add",
            path = "device/{address}/reading",
            httpMethod = ApiMethod.HttpMethod.POST,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public void add(
            User user,
            HttpServletRequest request,
            @Named("node") String nodeId,
            @Named("address") String address,
            @Named("readingValue") Double readingValue) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        if(nodeId.contentEquals(TrackerNodeEndpoint.CurrentString)) {
            nodeId = request.getRemoteAddr();
        }
        address = URLDecoder.decode(address);
        TrackerController.addReading(nodeId, address, readingValue);
    }
}
