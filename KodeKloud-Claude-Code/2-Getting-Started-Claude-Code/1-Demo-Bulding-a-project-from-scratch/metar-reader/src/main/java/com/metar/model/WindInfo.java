package com.metar.model;

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

    public WindInfo() {}

    public String getRawDirection() { return rawDirection; }
    public void setRawDirection(String rawDirection) { this.rawDirection = rawDirection; }

    public int getDirectionDegrees() { return directionDegrees; }
    public void setDirectionDegrees(int directionDegrees) { this.directionDegrees = directionDegrees; }

    public String getDirectionDescription() { return directionDescription; }
    public void setDirectionDescription(String directionDescription) { this.directionDescription = directionDescription; }

    public int getSpeedKt() { return speedKt; }
    public void setSpeedKt(int speedKt) { this.speedKt = speedKt; }

    public Integer getGustKt() { return gustKt; }
    public void setGustKt(Integer gustKt) { this.gustKt = gustKt; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public boolean isCalm() { return calm; }
    public void setCalm(boolean calm) { this.calm = calm; }

    public boolean isVariable() { return variable; }
    public void setVariable(boolean variable) { this.variable = variable; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getVariableFrom() { return variableFrom; }
    public void setVariableFrom(Integer variableFrom) { this.variableFrom = variableFrom; }

    public Integer getVariableTo() { return variableTo; }
    public void setVariableTo(Integer variableTo) { this.variableTo = variableTo; }
}
