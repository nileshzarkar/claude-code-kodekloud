package com.metar.model;

/**
 * Holds all wind-related data decoded from a METAR report.
 * Speeds are normalized to knots regardless of the original unit (KT or MPS).
 * When the wind direction is variable over a range, both boundary bearings are stored.
 */
public class WindInfo {
    private String rawDirection;
    private int directionDegrees;
    private String directionDescription;
    private int speedKt;
    private Integer gustKt;
    private String unit;
    private boolean calm;
    private boolean variable;
    private String description;
    private Integer variableFrom;
    private Integer variableTo;

    /** Creates an empty WindInfo; fields are populated by {@code MetarDecoder}. */
    public WindInfo() {}

    /** Returns the raw direction string from the METAR token (e.g. "270", "VRB", "000"). */
    public String getRawDirection() { return rawDirection; }

    /** Sets the raw direction string. */
    public void setRawDirection(String rawDirection) { this.rawDirection = rawDirection; }

    /** Returns the wind direction in degrees true (0–360), or -1 if variable. */
    public int getDirectionDegrees() { return directionDegrees; }

    /** Sets the wind direction in degrees true. */
    public void setDirectionDegrees(int directionDegrees) { this.directionDegrees = directionDegrees; }

    /** Returns the human-readable compass direction (e.g. "West (270°)"). */
    public String getDirectionDescription() { return directionDescription; }

    /** Sets the human-readable compass direction description. */
    public void setDirectionDescription(String directionDescription) { this.directionDescription = directionDescription; }

    /** Returns the sustained wind speed in knots (converted from MPS if necessary). */
    public int getSpeedKt() { return speedKt; }

    /** Sets the sustained wind speed in knots. */
    public void setSpeedKt(int speedKt) { this.speedKt = speedKt; }

    /** Returns the gust speed in knots, or {@code null} if no gusts are reported. */
    public Integer getGustKt() { return gustKt; }

    /** Sets the gust speed in knots. */
    public void setGustKt(Integer gustKt) { this.gustKt = gustKt; }

    /** Returns the original speed unit from the METAR ("KT" for knots, "MPS" for meters per second). */
    public String getUnit() { return unit; }

    /** Sets the original speed unit. */
    public void setUnit(String unit) { this.unit = unit; }

    /** Returns {@code true} if the wind is calm (speed 0 and direction 000 or VRB). */
    public boolean isCalm() { return calm; }

    /** Sets whether the wind is calm. */
    public void setCalm(boolean calm) { this.calm = calm; }

    /** Returns {@code true} if the wind direction is variable (VRB prefix). */
    public boolean isVariable() { return variable; }

    /** Sets whether the wind direction is variable. */
    public void setVariable(boolean variable) { this.variable = variable; }

    /** Returns the full human-readable wind description including direction, speed, and gusts. */
    public String getDescription() { return description; }

    /** Sets the full human-readable wind description. */
    public void setDescription(String description) { this.description = description; }

    /** Returns the lower bound of a variable wind direction range in degrees, or {@code null} if not variable. */
    public Integer getVariableFrom() { return variableFrom; }

    /** Sets the lower bound of the variable wind direction range. */
    public void setVariableFrom(Integer variableFrom) { this.variableFrom = variableFrom; }

    /** Returns the upper bound of a variable wind direction range in degrees, or {@code null} if not variable. */
    public Integer getVariableTo() { return variableTo; }

    /** Sets the upper bound of the variable wind direction range. */
    public void setVariableTo(Integer variableTo) { this.variableTo = variableTo; }
}
