package com.metar.model;

public class CloudLayer {
    private String coverage;
    private String coverageDescription;
    private int altitudeFt;
    private String cloudType;
    private String description;

    public CloudLayer() {}

    public CloudLayer(String coverage, String coverageDescription, int altitudeFt, String cloudType, String description) {
        this.coverage = coverage;
        this.coverageDescription = coverageDescription;
        this.altitudeFt = altitudeFt;
        this.cloudType = cloudType;
        this.description = description;
    }

    public String getCoverage() { return coverage; }
    public void setCoverage(String coverage) { this.coverage = coverage; }

    public String getCoverageDescription() { return coverageDescription; }
    public void setCoverageDescription(String coverageDescription) { this.coverageDescription = coverageDescription; }

    public int getAltitudeFt() { return altitudeFt; }
    public void setAltitudeFt(int altitudeFt) { this.altitudeFt = altitudeFt; }

    public String getCloudType() { return cloudType; }
    public void setCloudType(String cloudType) { this.cloudType = cloudType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
