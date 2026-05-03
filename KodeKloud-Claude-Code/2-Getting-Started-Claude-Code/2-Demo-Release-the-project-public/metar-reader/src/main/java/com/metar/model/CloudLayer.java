package com.metar.model;

/**
 * Represents a single cloud layer parsed from a METAR report.
 * Stores the coverage code (e.g. FEW, SCT, BKN, OVC), its human-readable description,
 * the cloud base altitude in feet, and an optional cloud type (CB or TCU).
 */
public class CloudLayer {
    private String coverage;
    private String coverageDescription;
    private int altitudeFt;
    private String cloudType;
    private String description;

    /** Creates an empty CloudLayer; fields must be set individually via setters. */
    public CloudLayer() {}

    /**
     * Creates a fully populated CloudLayer in a single call.
     *
     * @param coverage            METAR cloud coverage code (e.g. "BKN")
     * @param coverageDescription human-readable description of the coverage amount
     * @param altitudeFt          cloud base altitude in feet above ground level
     * @param cloudType           optional significant cloud type ("CB" or "TCU"), or {@code null}
     * @param description         full one-line description combining coverage, altitude, and type
     */
    public CloudLayer(String coverage, String coverageDescription, int altitudeFt, String cloudType, String description) {
        this.coverage = coverage;
        this.coverageDescription = coverageDescription;
        this.altitudeFt = altitudeFt;
        this.cloudType = cloudType;
        this.description = description;
    }

    /** Returns the METAR cloud coverage code (e.g. "FEW", "SCT", "BKN", "OVC"). */
    public String getCoverage() { return coverage; }

    /** Sets the METAR cloud coverage code. */
    public void setCoverage(String coverage) { this.coverage = coverage; }

    /** Returns the human-readable description of the sky coverage fraction. */
    public String getCoverageDescription() { return coverageDescription; }

    /** Sets the human-readable coverage description. */
    public void setCoverageDescription(String coverageDescription) { this.coverageDescription = coverageDescription; }

    /** Returns the cloud base altitude in feet above ground level. */
    public int getAltitudeFt() { return altitudeFt; }

    /** Sets the cloud base altitude in feet above ground level. */
    public void setAltitudeFt(int altitudeFt) { this.altitudeFt = altitudeFt; }

    /** Returns the significant cloud type ("CB" for Cumulonimbus, "TCU" for Towering Cumulus), or {@code null} if absent. */
    public String getCloudType() { return cloudType; }

    /** Sets the significant cloud type. */
    public void setCloudType(String cloudType) { this.cloudType = cloudType; }

    /** Returns the full human-readable description of this cloud layer. */
    public String getDescription() { return description; }

    /** Sets the full human-readable description of this cloud layer. */
    public void setDescription(String description) { this.description = description; }
}
