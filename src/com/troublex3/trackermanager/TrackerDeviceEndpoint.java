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
import java.net.URLDecoder;


/**
 * Created by rodtoll on 8/13/14.
 */
@Api(name="devices", version="v1", description = "Service for Tracker Device Management")
public class TrackerDeviceEndpoint {
    @ApiMethod(
            name = "devices.list",
            path = "devices",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public Collection<TrackerDevice> deviceList(User user, HttpServletRequest request) throws UnauthorizedException {
        TrackerAuthentication.authenticateOrThrow(user, request);
        return TrackerStore.getDevicesSeen();
    }

    @ApiMethod(
            name = "devices.getDevice",
            path = "devices/{address}",
            httpMethod = ApiMethod.HttpMethod.GET,
            scopes = {TrackerAuthentication.EMAIL_SCOPE},
            clientIds = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
    )
    public TrackerDevice getDevicePresent(User user, HttpServletRequest request, @Named("address") String address) throws UnauthorizedException, UnsupportedEncodingException {
        TrackerAuthentication.authenticateOrThrow(user, request);

        if(SystemProperty.environment.value() != SystemProperty.Environment.Value.Production) {
            address = URLDecoder.decode(address, "UTF-8");
        }

        TrackerDevice device = TrackerStore.getDevice(address);
        return device;
    }
}
