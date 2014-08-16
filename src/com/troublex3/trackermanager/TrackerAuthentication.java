package com.troublex3.trackermanager;

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rodtoll on 7/26/14.
 */
public class TrackerAuthentication {
    public static final String OTHER_CLIENT_ID = "519766688288-kr05dkpcn39stnpvfq4s0boek3jeh22e.apps.googleusercontent.com";
    public static final String APP_ENGINE_CLIENT_ID = "519766688288-kr05dkpcn39stnpvfq4s0boek3jeh22e.apps.googleusercontent.com";
    public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    public static final String DEVICE_SCOPE = "http://oauth.net/grant_type/device/1.0";
    public static final String AUTHENTICATION_HEADER = "x-troublex3-bluetracker-auth";
    public static final String ALLOWED_EMAIL = "rod.toll@gmail.com";
    public static final String ALLOWED_EMAIL_TEST = "example@example.com";
    public static final String [] ALLOWED_SCOPES = {TrackerAuthentication.EMAIL_SCOPE, TrackerAuthentication.DEVICE_SCOPE};

    public static final String [] ALLOWED_CLIENTIDS = { TrackerAuthentication.OTHER_CLIENT_ID, TrackerAuthentication.APP_ENGINE_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID};

    public static void authenticateOrThrow(User user, HttpServletRequest request) throws UnauthorizedException {
        if(user != null) {
            if(!user.getEmail().contentEquals(TrackerAuthentication.ALLOWED_EMAIL_TEST) &&
               !user.getEmail().contains(TrackerAuthentication.ALLOWED_EMAIL) ) {
                throw new UnauthorizedException("Unknown user. No authentication allowed");
            }
        } else {
            String headerValue = request.getHeader(TrackerAuthentication.AUTHENTICATION_HEADER);
            if(headerValue == null) {
                throw new UnauthorizedException("Unknown user. No user and no authentication header");
            }
        }
    }

}
