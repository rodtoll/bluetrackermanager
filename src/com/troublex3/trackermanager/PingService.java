package com.troublex3.trackermanager;

/**
 * Created by rodtoll on 9/1/14.
 */
public class PingService {

    public PingService() {}

    public PingService(PingService copyFrom) {
        this.setSleepPeriod(copyFrom.getSleepPeriod());
        this.setTimeout(copyFrom.getTimeout());
        this.setRetries(copyFrom.getRetries());
        this.setRetryPause(copyFrom.getRetryPause());
    }

    private Integer sleepPeriod;
    private Integer timeout;
    private Integer retries;
    private Integer retryPause;

    public Integer getSleepPeriod() {
        return sleepPeriod;
    }

    public void setSleepPeriod(Integer sleepPeriod) {
        this.sleepPeriod = sleepPeriod;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public Integer getRetryPause() {
        return retryPause;
    }

    public void setRetryPause(Integer retryPause) {
        this.retryPause = retryPause;
    }
}
