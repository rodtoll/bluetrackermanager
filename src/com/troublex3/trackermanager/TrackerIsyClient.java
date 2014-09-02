package com.troublex3.trackermanager;

import sun.net.www.protocol.http.*;

import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by rodtoll on 8/30/14.
 */
public class TrackerIsyClient {

    private HttpURLConnection isyConnection;
    private String userName;
    private String password;
    private String address;

    public TrackerIsyClient(String _userName, String _password, String _address)
    {
        this.userName = _userName;
        this.password = _password;
        this.address = _address;
    }

    String streamToString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        for(String line = br.readLine(); line != null; line = br.readLine())
            out.append(line);
        br.close();
        return out.toString();
    }

    public Boolean setVariable(Integer variableId, Integer valueToSet) throws MalformedURLException, IOException {

        URL url = new URL("http://"+this.address+"/rest/vars/set/2/"+variableId.toString()+"/"+valueToSet.toString());
        URLConnection connection = url.openConnection();
        String userpass = this.userName + ":" + this.password;
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
        connection.setRequestProperty ("Authorization", basicAuth);
        connection.setRequestProperty("Accept", "application/xml");
        InputStream response = connection.getInputStream();
        String responseContents = streamToString(response);
        return true;
    }


}
