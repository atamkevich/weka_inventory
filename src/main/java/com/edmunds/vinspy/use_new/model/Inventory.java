package com.edmunds.vinspy.use_new.model;


import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents inventory domain object.
 *
 * @author zenind
 */

@Document
public class Inventory extends BaseEntity<String> {

    @NotNull
    private Date processed;
    @NotNull
    private Map<ExtractedAttr, ?> attributes;

    private Status status;

    private List<Location> locations;

    private List<History> history;

    private InventoryType inventoryType;

    private String inventoryTypeWeka;

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public Date getProcessed() {
        return processed;
    }

    public void setProcessed(Date processed) {
        this.processed = processed;
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getInventoryTypeWeka() {
        return inventoryTypeWeka;
    }

    public void setInventoryTypeWeka(String inventoryTypeWeka) {
        this.inventoryTypeWeka = inventoryTypeWeka;
    }
}
