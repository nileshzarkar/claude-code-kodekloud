package com.metar.model;

public class WeatherPhenomenon {
    private String code;
    private String description;

    public WeatherPhenomenon() {}

    public WeatherPhenomenon(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
