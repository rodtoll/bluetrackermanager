package com.troublex3.trackermanager;

/**
 * Created by rodtoll on 9/1/14.
 */
public class GarageService {

    public GarageService() {}

    private Integer pin;

    public GarageService(GarageService copyFrom) {
        this.setPin(copyFrom.getPin());
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
