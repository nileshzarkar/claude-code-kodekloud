package com.metar.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Maps the JSON response returned by the Aviation Weather API for a single METAR observation.
 * Unknown JSON fields are silently ignored to tolerate future API additions.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AviationWeatherResponse {

    @JsonProperty("icaoId")
    private String icaoId;

    @JsonProperty("rawOb")
    private String rawOb;

    @JsonProperty("name")
    private String name;

    /** Returns the ICAO station identifier (e.g. "KLAX"). */
    public String getIcaoId() { return icaoId; }

    /** Sets the ICAO station identifier. */
    public void setIcaoId(String icaoId) { this.icaoId = icaoId; }

    /** Returns the raw METAR observation string as published by the station. */
    public String getRawOb() { return rawOb; }

    /** Sets the raw METAR observation string. */
    public void setRawOb(String rawOb) { this.rawOb = rawOb; }

    /** Returns the human-readable airport or station name. */
    public String getName() { return name; }

    /** Sets the human-readable airport or station name. */
    public void setName(String name) { this.name = name; }
}
