package com.metar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AviationWeatherResponse {

    @JsonProperty("icaoId")
    private String icaoId;

    @JsonProperty("rawOb")
    private String rawOb;

    @JsonProperty("name")
    private String name;

    public String getIcaoId() { return icaoId; }
    public void setIcaoId(String icaoId) { this.icaoId = icaoId; }

    public String getRawOb() { return rawOb; }
    public void setRawOb(String rawOb) { this.rawOb = rawOb; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
