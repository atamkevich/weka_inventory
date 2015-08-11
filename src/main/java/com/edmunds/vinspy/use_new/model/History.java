package com.edmunds.vinspy.use_new.model;

import java.util.Date;
import java.util.Map;

/**
 * @author Vitaly_Krasovsky
 */
public class History {
    private long jobId;
    private Date processed;
    private Map<ExtractedAttr, ?> attributes;
    private Location locations;
    private Status status;

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public Map<ExtractedAttr, ?> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<ExtractedAttr, ?> attributes) {
        this.attributes = attributes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Location getLocations() {
        return locations;
    }

    public void setLocations(Location locations) {
        this.locations = locations;
    }

    public Date getProcessed() {
        return processed;
    }

    public void setProcessed(Date processed) {
        this.processed = processed;
    }
}
