package com.troublex3.trackermanager;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rodtoll on 7/25/14.
 */
public class TrackerNode {

    private String nodeId;
    private Date lastReading;
    private Date lastHeartbeat;

    public TrackerNode() {
        nodeId = "";
        lastReading = null;
        lastHeartbeat = null;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Date getLastReading() {
        return lastReading;
    }

    public void setLastReading(Date lastReading) {
        this.lastReading = lastReading;
        setLastHeartbeat(lastReading);
    }

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
}
