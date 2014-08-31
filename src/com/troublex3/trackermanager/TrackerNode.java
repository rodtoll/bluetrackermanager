package com.troublex3.trackermanager;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rodtoll on 7/25/14.
 */
public class TrackerNode {

    private String nodeId;
    private String address;
    private Date lastReading;
    private Date lastHeartbeat;
    private String location;
    private String capabilities;
    private String isyVariableName;
    private String nodeType;

    public TrackerNode() {
    }

    public TrackerNode(TrackerNode nodeToCopy) {
        setFrom(nodeToCopy);
    }

    public void setFrom(TrackerNode nodeToCopy) {
        setNodeId(nodeToCopy.getNodeId());
        setLastReading(nodeToCopy.getLastReading());
        setLastHeartbeat(nodeToCopy.getLastHeartbeat());
        setLocation(nodeToCopy.getLocation());
        setCapabilities(nodeToCopy.getCapabilities());
        setAddress(nodeToCopy.getAddress());
        setIsyVariableName(nodeToCopy.getIsyVariableName());
        setNodeType(nodeToCopy.getNodeType());
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
    }

    public Date getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(Date lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsyVariableName() {
        return isyVariableName;
    }

    public void setIsyVariableName(String isyVariableName) {
        this.isyVariableName = isyVariableName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
