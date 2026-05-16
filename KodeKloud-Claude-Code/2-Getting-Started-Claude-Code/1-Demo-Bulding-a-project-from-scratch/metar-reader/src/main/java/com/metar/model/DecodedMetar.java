package com.metar.model;

import java.util.List;

public class DecodedMetar {
    public String station;
    public String rawMetar;
    public String observationTime;
    public WindInfo wind;
    public String visibilityDescription;
    public List<String> weatherPhenomena;
    public List<CloudLayer> cloudLayers;
    public int temperatureCelsius;
    public int temperatureFahrenheit;
    public int dewPointCelsius;
    public int dewPointFahrenheit;
    public double pressureHpa;
    public double pressureInHg;
    public String flightCategory;
    public String friendlyReport;
    public String error;
}
