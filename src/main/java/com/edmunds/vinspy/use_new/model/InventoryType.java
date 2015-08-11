package com.edmunds.vinspy.use_new.model;

/**
 * @author Vitaly_Krasovsky
 */
public enum InventoryType {
    NEW("new"),
    USED("used"),
    CERTIFIED("certified");

    private String inventoryType;

    InventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public static InventoryType value(String name) {
        try {
            return Enum.valueOf(InventoryType.class, name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
