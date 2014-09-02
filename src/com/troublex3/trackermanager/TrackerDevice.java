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
    private Integer isyVariableId;
    private Boolean isPresent;
    private Integer reportingInterval;
    private Integer timeoutInterval;
    private String deviceType;

    public TrackerDevice() {
        setIsPresent(null);
    }

    public TrackerDevice(String _address,
                         String _friendlyName,
                         Date _lastSeen,
                         Integer _isyVariableId,
                         Integer _reportingInterval,
                         Integer _timeoutInterval
                         ) {
        setAddress(_address);
        setFriendlyName(_friendlyName);
        setLastSeen(_lastSeen);
        setLastReading(null);
        setIsyVariableId(_isyVariableId);
        setIsPresent(null);
        setReportingInterval(_reportingInterval);
        setTimeoutInterval(_timeoutInterval);
        setDeviceType("Dynamic");
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
        setIsyVariableId(deviceToCopy.getIsyVariableId());
        setIsPresent(deviceToCopy.getIsPresent());
        setTimeoutInterval(deviceToCopy.getTimeoutInterval());
        setReportingInterval(deviceToCopy.getReportingInterval());
        setDeviceType(deviceToCopy.getDeviceType());
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
        if(this.lastReading != null) {
            this.setLastSeen(reading.getTimeStamp());
        }
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

    public Integer getIsyVariableId() {
        return this.isyVariableId;
    }

    public void setIsyVariableId(Integer _isyVariableId) {
        this.isyVariableId = _isyVariableId;
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

    public Boolean checkPresence(Date nowTime) {
        if(getLastSeen() == null) {
            return false;
        }
        long difference = nowTime.getTime() - getLastSeen().getTime();
        long differenceInS = (difference / 1000);
        return (differenceInS <= getTimeoutInterval());
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
