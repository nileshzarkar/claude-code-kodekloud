package com.metar.model;

import java.util.List;

/**
 * The primary output model of the METAR decoder. Contains every field extracted from a raw METAR
 * observation string, including both the original coded values and their human-readable descriptions.
 * Also holds derived values such as relative humidity and FAA flight category.
 * If decoding fails on an empty or null input, only {@code rawMetar} and {@code error} are set.
 */
public class DecodedMetar {
    private String rawMetar;
    private String reportType;
    private String reportTypeDescription;
    private String stationId;
    private String stationName;
    private String dateTime;
    private String dateTimeDescription;
    private WindInfo wind;
    private String visibility;
    private String visibilityDescription;
    private Integer visibilityMeters;
    private boolean cavok;
    private List<WeatherPhenomenon> weatherPhenomena;
    private List<CloudLayer> cloudLayers;
    private String skyClear;
    private String skyClearDescription;
    private Integer verticalVisibility;
    private String verticalVisibilityDescription;
    private Double temperature;
    private Double dewPoint;
    private Long relativeHumidity;
    private String temperatureDescription;
    private String altimeter;
    private String altimeterDescription;
    private String remarks;
    private String remarksDescription;
    private String flightCategory;
    private String flightCategoryDescription;
    private String error;

    /** Returns the original, unmodified METAR observation string. */
    public String getRawMetar() { return rawMetar; }

    /** Sets the original METAR observation string. */
    public void setRawMetar(String rawMetar) { this.rawMetar = rawMetar; }

    /** Returns the report type code ("METAR" or "SPECI"). */
    public String getReportType() { return reportType; }

    /** Sets the report type code. */
    public void setReportType(String reportType) { this.reportType = reportType; }

    /** Returns a human-readable explanation of the report type. */
    public String getReportTypeDescription() { return reportTypeDescription; }

    /** Sets the human-readable report type description. */
    public void setReportTypeDescription(String reportTypeDescription) { this.reportTypeDescription = reportTypeDescription; }

    /** Returns the ICAO station identifier (e.g. "KLAX"). */
    public String getStationId() { return stationId; }

    /** Sets the ICAO station identifier. */
    public void setStationId(String stationId) { this.stationId = stationId; }

    /** Returns the human-readable airport/station name. */
    public String getStationName() { return stationName; }

    /** Sets the human-readable airport/station name. */
    public void setStationName(String stationName) { this.stationName = stationName; }

    /** Returns the raw date-time token from the METAR (e.g. "011755Z"). */
    public String getDateTime() { return dateTime; }

    /** Sets the raw date-time token. */
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    /** Returns a human-readable description of the observation date and time in UTC. */
    public String getDateTimeDescription() { return dateTimeDescription; }

    /** Sets the human-readable date-time description. */
    public void setDateTimeDescription(String dateTimeDescription) { this.dateTimeDescription = dateTimeDescription; }

    /** Returns the decoded wind information, or {@code null} if not reported. */
    public WindInfo getWind() { return wind; }

    /** Sets the decoded wind information. */
    public void setWind(WindInfo wind) { this.wind = wind; }

    /** Returns the raw visibility token (e.g. "9999", "10SM", "1/2SM", "CAVOK"). */
    public String getVisibility() { return visibility; }

    /** Sets the raw visibility token. */
    public void setVisibility(String visibility) { this.visibility = visibility; }

    /** Returns a human-readable description of the prevailing visibility. */
    public String getVisibilityDescription() { return visibilityDescription; }

    /** Sets the human-readable visibility description. */
    public void setVisibilityDescription(String visibilityDescription) { this.visibilityDescription = visibilityDescription; }

    /** Returns the prevailing visibility converted to meters, or {@code null} if not available. */
    public Integer getVisibilityMeters() { return visibilityMeters; }

    /** Sets the prevailing visibility in meters. */
    public void setVisibilityMeters(Integer visibilityMeters) { this.visibilityMeters = visibilityMeters; }

    /** Returns {@code true} if the METAR reports CAVOK (Ceiling And Visibility OK). */
    public boolean isCavok() { return cavok; }

    /** Sets whether the METAR reports CAVOK. */
    public void setCavok(boolean cavok) { this.cavok = cavok; }

    /** Returns the list of decoded weather phenomena, or {@code null} if none reported. */
    public List<WeatherPhenomenon> getWeatherPhenomena() { return weatherPhenomena; }

    /** Sets the list of decoded weather phenomena. */
    public void setWeatherPhenomena(List<WeatherPhenomenon> weatherPhenomena) { this.weatherPhenomena = weatherPhenomena; }

    /** Returns the list of decoded cloud layers, or {@code null} if none reported. */
    public List<CloudLayer> getCloudLayers() { return cloudLayers; }

    /** Sets the list of decoded cloud layers. */
    public void setCloudLayers(List<CloudLayer> cloudLayers) { this.cloudLayers = cloudLayers; }

    /** Returns the sky-clear code (SKC, CLR, NSC, or NCD) when no cloud layers are present, or {@code null}. */
    public String getSkyClear() { return skyClear; }

    /** Sets the sky-clear code. */
    public void setSkyClear(String skyClear) { this.skyClear = skyClear; }

    /** Returns the human-readable description of the sky-clear condition. */
    public String getSkyClearDescription() { return skyClearDescription; }

    /** Sets the human-readable sky-clear description. */
    public void setSkyClearDescription(String skyClearDescription) { this.skyClearDescription = skyClearDescription; }

    /** Returns the vertical visibility in feet when the sky is obscured (VV prefix), or {@code null}. */
    public Integer getVerticalVisibility() { return verticalVisibility; }

    /** Sets the vertical visibility in feet. */
    public void setVerticalVisibility(Integer verticalVisibility) { this.verticalVisibility = verticalVisibility; }

    /** Returns a human-readable description of the vertical visibility condition. */
    public String getVerticalVisibilityDescription() { return verticalVisibilityDescription; }

    /** Sets the human-readable vertical visibility description. */
    public void setVerticalVisibilityDescription(String verticalVisibilityDescription) { this.verticalVisibilityDescription = verticalVisibilityDescription; }

    /** Returns the air temperature in degrees Celsius. */
    public Double getTemperature() { return temperature; }

    /** Sets the air temperature in degrees Celsius. */
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    /** Returns the dew point temperature in degrees Celsius. */
    public Double getDewPoint() { return dewPoint; }

    /** Sets the dew point temperature in degrees Celsius. */
    public void setDewPoint(Double dewPoint) { this.dewPoint = dewPoint; }

    /** Returns the calculated relative humidity as a percentage (0–100). */
    public Long getRelativeHumidity() { return relativeHumidity; }

    /** Sets the calculated relative humidity percentage. */
    public void setRelativeHumidity(Long relativeHumidity) { this.relativeHumidity = relativeHumidity; }

    /** Returns a combined human-readable string for temperature and dew point in both °C and °F. */
    public String getTemperatureDescription() { return temperatureDescription; }

    /** Sets the combined temperature/dew point description. */
    public void setTemperatureDescription(String temperatureDescription) { this.temperatureDescription = temperatureDescription; }

    /** Returns the raw altimeter setting token (e.g. "Q1013", "A2992"). */
    public String getAltimeter() { return altimeter; }

    /** Sets the raw altimeter setting token. */
    public void setAltimeter(String altimeter) { this.altimeter = altimeter; }

    /** Returns a human-readable description of the altimeter setting in both hPa and inHg. */
    public String getAltimeterDescription() { return altimeterDescription; }

    /** Sets the human-readable altimeter description. */
    public void setAltimeterDescription(String altimeterDescription) { this.altimeterDescription = altimeterDescription; }

    /** Returns the full remarks section starting from "RMK", or {@code null} if absent. */
    public String getRemarks() { return remarks; }

    /** Sets the remarks section. */
    public void setRemarks(String remarks) { this.remarks = remarks; }

    /** Returns a note that the remarks section is not further decoded. */
    public String getRemarksDescription() { return remarksDescription; }

    /** Sets the remarks description note. */
    public void setRemarksDescription(String remarksDescription) { this.remarksDescription = remarksDescription; }

    /** Returns the FAA flight category code: VFR, MVFR, IFR, or LIFR. */
    public String getFlightCategory() { return flightCategory; }

    /** Sets the FAA flight category code. */
    public void setFlightCategory(String flightCategory) { this.flightCategory = flightCategory; }

    /** Returns a human-readable description of the flight category and the conditions that determined it. */
    public String getFlightCategoryDescription() { return flightCategoryDescription; }

    /** Sets the human-readable flight category description. */
    public void setFlightCategoryDescription(String flightCategoryDescription) { this.flightCategoryDescription = flightCategoryDescription; }

    /** Returns an error message if decoding failed (e.g. empty input), or {@code null} on success. */
    public String getError() { return error; }

    /** Sets the error message. */
    public void setError(String error) { this.error = error; }
}
