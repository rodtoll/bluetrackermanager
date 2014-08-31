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
    private TrackerReading lastReading;
    private String isyVariableName;
    private Boolean isPresent;
    private Integer reportingInterval;
    private Integer timeoutInterval;

    public TrackerDevice() {

    }

    public TrackerDevice(String _address,
                         String _friendlyName,
                         Date _lastSeen,
                         String _isyVariableName,
                         Integer _reportingInterval,
                         Integer _timeoutInterval
                         ) {
        setAddress(_address);
        setFriendlyName(_friendlyName);
        setLastSeen(_lastSeen);
        setLastReading(null);
        setIsyVariableName(_isyVariableName);
        setIsPresent(false);
        setReportingInterval(_reportingInterval);
        setTimeoutInterval(_timeoutInterval);
    }

    public TrackerDevice(TrackerDevice deviceToCopy) {
        setFrom(deviceToCopy);
    }

    public void setFrom(TrackerDevice deviceToCopy) {
        setAddress(deviceToCopy.getAddress());
        setFriendlyName(deviceToCopy.getFriendlyName());
        setLastSeen(deviceToCopy.getLastSeen());
        if(deviceToCopy.getLastReading() != null) {
            setLastReading(new TrackerReading(deviceToCopy.getLastReading()));
        } else {
            setLastReading(null);
        }
        setIsyVariableName(deviceToCopy.getIsyVariableName());
        setIsPresent(deviceToCopy.getIsPresent());
        setTimeoutInterval(deviceToCopy.getTimeoutInterval());
        setReportingInterval(deviceToCopy.getReportingInterval());
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

    public TrackerReading getLastReading() { return this.lastReading; }

    public void clearPresence() {
        this.lastReading = null;
        this.setLastSeen(null);
    }

    public void setLastReading(TrackerReading reading) {
        this.lastReading = reading;
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean present) {
        this.isPresent = present;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getIsyVariableName() {
        return isyVariableName;
    }

    public void setIsyVariableName(String isyVariableName) {
        this.isyVariableName = isyVariableName;
    }

    public Integer getReportingInterval() {
        return reportingInterval;
    }

    public void setReportingInterval(Integer reportingInterval) {
        this.reportingInterval = reportingInterval;
    }

    public Integer getTimeoutInterval() {
        return timeoutInterval;
    }

    public void setTimeoutInterval(Integer timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
    }
}
