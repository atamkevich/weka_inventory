package com.edmunds.vinspy.use_new.model;

import java.util.Objects;

/**
 * @author zenind
 */
public class Location {

    private Long locationId;

    private Long siteId;

    private Long templateId;

    private String url;

    private String siteUrl;

    private Location() {
    }

    public Location(Long locationId, Long siteId, String url, String siteUrl, Long templateId) {
        this.locationId = locationId;
        this.siteId = siteId;
        this.url = url;
        this.siteUrl = siteUrl;
        this.templateId = templateId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Objects.equals(locationId, location.locationId) &&
            Objects.equals(siteId, location.siteId) &&
            Objects.equals(url, location.url) &&
            Objects.equals(siteUrl, location.siteUrl) &&
            Objects.equals(templateId, location.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, siteId, url, siteUrl, templateId);
    }
}
