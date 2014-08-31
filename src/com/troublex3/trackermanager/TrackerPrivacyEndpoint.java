package com.troublex3.trackermanager;

/**
 * Created by rodtoll on 8/17/14.
 */

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rodtoll on 8/13/14.
 */
@Api(name="tracker", version="v1", description = "Service for Trackers")
public class TrackerPrivacyEndpoint {
    @ApiMethod(
            name = "privacy.enable",
            path = "privacy/{id}/{state}",
            httpMethod =  ApiMethod.HttpMethod.GET
    )
    public void heartbeat(User user, HttpServletRequest request, @Named("id") String id, @Named("state") Boolean enabled) throws UnauthorizedException {
        if(id.contentEquals("joanne")) {
        } else {
            throw new UnauthorizedException("Specified id is not valid and auth failed");
        }
    }
}
