package com.troublex3.trackermanager;

/**
 * Created by rodtoll on 9/1/14.
 */
public class RHTService {

    public RHTService() {}

    public RHTService(RHTService copyFrom) {
        this.setBaseAddress(copyFrom.getBaseAddress());
        this.setSleep(copyFrom.getSleep());
    }

    private String baseAddress;
    private Integer sleep;

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }
}
