package com.troublex3.trackermanager;

import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.Date;

/**
 * Created by rodtoll on 8/15/14.
 */
public class TrackerSmsEndpoint extends HttpServlet {

    public static final String TwilloNodeName = "twilio";
    public static final String TwilloFromField = "From";
    public static final String EnteredKeyword = "entered";
    public static final String ConnectedKeyword = "connected";
    public static final String HomeKeyword = "1";
    public static final String WorkKeyword = "2";
    public static final Double HomeStateValue = 1.0;
    public static final Double WorkStateValue = 2.0;
    public static final Double UnknownStateValue = 3.0;
    public static final Double ExitedStateValue = 0.0;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String textContent = request.getParameter("Body");
        Double stateToWrite = TrackerSmsEndpoint.UnknownStateValue;

        if(textContent.contains(TrackerSmsEndpoint.EnteredKeyword) || textContent.contains(TrackerSmsEndpoint.ConnectedKeyword)) {
            if(textContent.contains(TrackerSmsEndpoint.HomeKeyword)) {
                stateToWrite = TrackerSmsEndpoint.HomeStateValue;
            } else if(textContent.contains(TrackerSmsEndpoint.WorkKeyword)) {
                stateToWrite = TrackerSmsEndpoint.WorkStateValue;
            } else {
                stateToWrite = TrackerSmsEndpoint.UnknownStateValue;
            }
        } else {
            stateToWrite = TrackerSmsEndpoint.ExitedStateValue;
        }

        TrackerController.addReading(TrackerSmsEndpoint.TwilloNodeName,
                request.getParameter(TrackerSmsEndpoint.TwilloFromField),
                stateToWrite);

        response.setContentType("text/xml");
        response.getWriter().print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<Response>\n" +
                "</Response>");
     }
}
