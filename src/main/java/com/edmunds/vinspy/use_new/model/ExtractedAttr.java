package com.edmunds.vinspy.use_new.model;

import java.math.BigDecimal;

/**
 * All attributes that should be extracted
 *
 * @author aliaksandr_vasiliuk
 * @author Aliaksandr_Nozdryn-Platnitski1
 */
public enum ExtractedAttr {
    VIN("meta_vinspy-vin", 1.0f, "$.vin", 0.0f),
    TITLE("meta_vinspy-title", 0.7f, "", 0.0f),
    MAKE("meta_vinspy-make", 0.3f, "$.results.make", 0.0f),
    MODEL("meta_vinspy-model", 0.3f, "$.results.model", 0.0f),
    YEAR("meta_vinspy-year", 0.3f, "$.results.year", 0.3f),
    OEM("meta_vinspy-oem", 0.3f, "$.input.oemModelCode", 0.0f),
    TRIM("meta_vinspy-trim", 0.3f, "$.results.trim", 0.0f),
    COLOR("meta_vinspy-color", 0.3f, "$.input.extColorName", 0.0f),
    ENGINE("meta_vinspy-engine", 0.3f, "$.input.engine", 0.0f),
    TRANSMISSION("meta_vinspy-transmission", 0.3f, "$.input.transmission", 0.0f),
    OPTIONS("meta_vinspy-options", 0.3f, "$.results.vehicleOptions", 0.0f),
    INTERIOR_COLOR("meta_vinspy-int-color", 0.3f, "$.input.intColorName", 0.0f),
    MILEAGE("meta_vinspy-mileage", 0.0f, "", 0.7f),
    MSRP("meta_vinspy-msrp", 0.0f, "", 0.7f),
    TYPE("meta_vinspy-type", 0.0f, "", 0.0f);

    /**
     * key for the storing metadata
     */
    private String metaDataKey;
    /** priority of the attr **/
    private BigDecimal priority;
    /**
     * priority of the attr when differentiate new/used cars
     */
    private BigDecimal priorityNewUsed;
    /** path to the appropriate property from the VinDescription service.
     * Uses JsonPath for the extracting attributes from the raw JSON.
     * If response of VinDecoder will be changed, this property must be changed accordingly
     *
     * @see <a href="https://github.com/jayway/JsonPath">https://github.com/jayway/JsonPath</a>
     * **/
    private String vinDecoderPath;


    ExtractedAttr(String metaDataKey, float priority, String vinDecoderPath, float priorityNewUsed) {
        this.metaDataKey = metaDataKey;
        this.priority = new BigDecimal(priority);
        this.vinDecoderPath = vinDecoderPath;
        this.priorityNewUsed = new BigDecimal(priorityNewUsed);
    }

    ExtractedAttr(String metaDataKey) {
        this.metaDataKey = metaDataKey;
    }

    public String getMetaDataKey() {
        return metaDataKey;
    }

    public BigDecimal getPriority() {
        return priority;
    }

    public String getVinDecoderPath() {
        return vinDecoderPath;
    }

    public BigDecimal getPriorityNewUsed() {
        return priorityNewUsed;
    }

    public static ExtractedAttr value(String name) {
        try {
            return Enum.valueOf(ExtractedAttr.class, name.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
