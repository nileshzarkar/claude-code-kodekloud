package com.metar.model;

/**
 * Represents a single weather phenomenon decoded from a METAR report.
 * Stores the original two-to-nine character code (e.g. "-RASN", "+TSRA", "VCFG")
 * alongside a human-readable description built by {@code MetarDecoder}.
 */
public class WeatherPhenomenon {
    private String code;
    private String description;

    /** Creates an empty WeatherPhenomenon; fields must be set via setters. */
    public WeatherPhenomenon() {}

    /**
     * Creates a WeatherPhenomenon with its raw code and decoded description.
     *
     * @param code        the original METAR weather token (e.g. "-RA", "+TSRA", "VCFG")
     * @param description human-readable expansion of the token (e.g. "Light Rain")
     */
    public WeatherPhenomenon(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /** Returns the original METAR weather phenomenon code. */
    public String getCode() { return code; }

    /** Sets the original METAR weather phenomenon code. */
    public void setCode(String code) { this.code = code; }

    /** Returns the human-readable description of the weather phenomenon. */
    public String getDescription() { return description; }

    /** Sets the human-readable description of the weather phenomenon. */
    public void setDescription(String description) { this.description = description; }
}
