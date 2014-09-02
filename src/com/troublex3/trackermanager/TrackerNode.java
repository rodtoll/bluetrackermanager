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
    private Integer isyVariableId;
    private String nodeType;
    private Boolean isPresent;
    private String deviceId;
    private PingService pingService;
    private RHTService rhtService;
    private GarageService garageService;

    public TrackerNode() {
        isPresent = null;
        lastReading = null;
        lastHeartbeat = null;
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
        setIsyVariableId(nodeToCopy.getIsyVariableId());
        setNodeType(nodeToCopy.getNodeType());
        setIsPresent(nodeToCopy.getIsPresent());
        setDeviceId(nodeToCopy.getDeviceId());
        if(nodeToCopy.getRhtService() != null) {
            setRhtService(new RHTService(nodeToCopy.getRhtService()));
        }
        if(nodeToCopy.getPingService() != null) {
            setPingService(new PingService(nodeToCopy.getPingService()));
        }
        if(nodeToCopy.getGarageService() != null) {
            setGarageService(new GarageService(nodeToCopy.getGarageService()));
        }
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

    public Integer getIsyVariableId() {
        return isyVariableId;
    }

    public void setIsyVariableId(Integer _isyVariableId) {
        this.isyVariableId = _isyVariableId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Boolean checkPresence(Date nowTime) {
        if(getLastHeartbeat() == null && getLastReading() == null) {
            return false;
        }
        Date dateToUse;

        if(getLastHeartbeat() == null) {
            dateToUse = getLastReading();
        } else if(getLastReading() == null) {
            dateToUse = getLastHeartbeat();
        } else if(getLastHeartbeat().getTime() > getLastReading().getTime()) {
            dateToUse = getLastHeartbeat();
        } else {
            dateToUse = getLastReading();
        }
        long difference = nowTime.getTime() - dateToUse.getTime();
        long differenceInS = (difference / 1000);
        return (differenceInS <= 60);
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public PingService getPingService() {
        return pingService;
    }

    public void setPingService(PingService pingService) {
        this.pingService = pingService;
    }

    public RHTService getRhtService() {
        return rhtService;
    }

    public void setRhtService(RHTService rhtService) {
        this.rhtService = rhtService;
    }

    public GarageService getGarageService() {
        return garageService;
    }

    public void setGarageService(GarageService garageService) {
        this.garageService = garageService;
    }
}
