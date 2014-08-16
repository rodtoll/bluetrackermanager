package com.troublex3.trackermanager;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rodtoll on 7/24/14.
 */
public class TrackerReading implements Comparable {

    private Date timeStamp;
    private String address;
    private Double signalStrength;

    public TrackerReading() {

    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(Double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public Double getSignalStrengthPositive() { return 100.0+signalStrength; }

    @Override
    public int compareTo(Object o) {
        TrackerReading otherReading = (TrackerReading) o;
        return getTimeStamp().compareTo(otherReading.getTimeStamp());
    }

    @Override
    public String toString() {
        return "TrackerReading { " +
                ", time = " + timeStamp.toString() +
                ", address = " + address +
                ", signal = " + signalStrength.toString() +
                " }";
    }
}