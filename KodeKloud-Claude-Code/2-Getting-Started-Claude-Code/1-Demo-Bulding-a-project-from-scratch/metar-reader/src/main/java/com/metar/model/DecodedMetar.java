package com.metar.model;

import java.util.List;

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

    public String getRawMetar() { return rawMetar; }
    public void setRawMetar(String rawMetar) { this.rawMetar = rawMetar; }

    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }

    public String getReportTypeDescription() { return reportTypeDescription; }
    public void setReportTypeDescription(String reportTypeDescription) { this.reportTypeDescription = reportTypeDescription; }

    public String getStationId() { return stationId; }
    public void setStationId(String stationId) { this.stationId = stationId; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getDateTimeDescription() { return dateTimeDescription; }
    public void setDateTimeDescription(String dateTimeDescription) { this.dateTimeDescription = dateTimeDescription; }

    public WindInfo getWind() { return wind; }
    public void setWind(WindInfo wind) { this.wind = wind; }

    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }

    public String getVisibilityDescription() { return visibilityDescription; }
    public void setVisibilityDescription(String visibilityDescription) { this.visibilityDescription = visibilityDescription; }

    public Integer getVisibilityMeters() { return visibilityMeters; }
    public void setVisibilityMeters(Integer visibilityMeters) { this.visibilityMeters = visibilityMeters; }

    public boolean isCavok() { return cavok; }
    public void setCavok(boolean cavok) { this.cavok = cavok; }

    public List<WeatherPhenomenon> getWeatherPhenomena() { return weatherPhenomena; }
    public void setWeatherPhenomena(List<WeatherPhenomenon> weatherPhenomena) { this.weatherPhenomena = weatherPhenomena; }

    public List<CloudLayer> getCloudLayers() { return cloudLayers; }
    public void setCloudLayers(List<CloudLayer> cloudLayers) { this.cloudLayers = cloudLayers; }

    public String getSkyClear() { return skyClear; }
    public void setSkyClear(String skyClear) { this.skyClear = skyClear; }

    public String getSkyClearDescription() { return skyClearDescription; }
    public void setSkyClearDescription(String skyClearDescription) { this.skyClearDescription = skyClearDescription; }

    public Integer getVerticalVisibility() { return verticalVisibility; }
    public void setVerticalVisibility(Integer verticalVisibility) { this.verticalVisibility = verticalVisibility; }

    public String getVerticalVisibilityDescription() { return verticalVisibilityDescription; }
    public void setVerticalVisibilityDescription(String verticalVisibilityDescription) { this.verticalVisibilityDescription = verticalVisibilityDescription; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getDewPoint() { return dewPoint; }
    public void setDewPoint(Double dewPoint) { this.dewPoint = dewPoint; }

    public Long getRelativeHumidity() { return relativeHumidity; }
    public void setRelativeHumidity(Long relativeHumidity) { this.relativeHumidity = relativeHumidity; }

    public String getTemperatureDescription() { return temperatureDescription; }
    public void setTemperatureDescription(String temperatureDescription) { this.temperatureDescription = temperatureDescription; }

    public String getAltimeter() { return altimeter; }
    public void setAltimeter(String altimeter) { this.altimeter = altimeter; }

    public String getAltimeterDescription() { return altimeterDescription; }
    public void setAltimeterDescription(String altimeterDescription) { this.altimeterDescription = altimeterDescription; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getRemarksDescription() { return remarksDescription; }
    public void setRemarksDescription(String remarksDescription) { this.remarksDescription = remarksDescription; }

    public String getFlightCategory() { return flightCategory; }
    public void setFlightCategory(String flightCategory) { this.flightCategory = flightCategory; }

    public String getFlightCategoryDescription() { return flightCategoryDescription; }
    public void setFlightCategoryDescription(String flightCategoryDescription) { this.flightCategoryDescription = flightCategoryDescription; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
