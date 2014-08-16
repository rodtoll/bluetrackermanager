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

    protected String mapRequestToSource(HttpServletRequest request) {
        String from = request.getParameter("From");
        if(from != null) {
            if (from.contentEquals("+14254459180")) {
                return "Rod MotoX SMS";
            } else if (from.contentEquals("+14258290621")) {
                return "Joanne Galaxy S5 SMS";
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        TrackerNode node = TrackerStore.getNode("twilio");

        String addressToUse = mapRequestToSource(request);
        if(addressToUse==null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        response.setContentType("text/plain");

        if(node != null) {

            String textContent = request.getParameter("Body");
            Integer stateToWrite = 0;

            if(textContent.contains("entered")) {

                if(textContent.contains("1")) {
                    stateToWrite = 1;
                } else if(textContent.contains("2")) {
                    stateToWrite = 2;
                } else {
                    stateToWrite = 3;
                }
            } else {
                stateToWrite = 0;
            }

            Date timeStamp = new Date();
            node.setLastHeartbeat(timeStamp);
            TrackerReading reading = new TrackerReading();
            reading.setAddress(addressToUse);
            request.getHeader(TrackerAuthentication.AUTHENTICATION_HEADER);
            reading.setSignalStrength(stateToWrite.doubleValue());
            reading.setTimeStamp(timeStamp);
            TrackerStore.addReading(node, reading);
            node.setLastReading(reading);
            TrackerStore.updateNode(node);
            response.getWriter().print(addressToUse + " -- " + stateToWrite.toString());
            return;
        }
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
