package com.troublex3.trackermanager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodtoll on 8/13/14.
 */
public class TrackerDevice {
    private String address;
    private String friendlyName;
    private Date lastSeen;
    private HashMap<String,Date> lastSeenMap;

    public TrackerDevice() {
        this.lastSeenMap = new HashMap<String, Date>();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void updateSeenMap(String seenBy, Date timeStamp) {
        this.lastSeenMap.put(seenBy,timeStamp);
        lastSeen = timeStamp;
    }

    public Map<String,Date> getSeenMap() {
        return this.lastSeenMap;
    }
}
