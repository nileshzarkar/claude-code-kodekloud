package com.metar.service;

import com.metar.client.AviationWeatherClient;
import com.metar.decoder.MetarDecoder;
import com.metar.model.AviationWeatherResponse;
import com.metar.model.DecodedMetar;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class MetarService {

    @RestClient
    AviationWeatherClient aviationWeatherClient;

    @Inject
    MetarDecoder decoder;

    public DecodedMetar getMetar(String airportCode) {
        String code = airportCode.toUpperCase().trim();

        if (code.length() < 3 || code.length() > 4) {
            throw new WebApplicationException(
                    Response.status(400).entity("{\"error\": \"Airport code must be 3-4 characters (ICAO format)\"}").build());
        }

        List<AviationWeatherResponse> responses;
        try {
            responses = aviationWeatherClient.getMetar(code, "json", false, 2);
        } catch (Exception e) {
            throw new WebApplicationException(
                    Response.status(503).entity("{\"error\": \"Unable to reach aviation weather service: " + e.getMessage() + "\"}").build());
        }

        if (responses == null || responses.isEmpty()) {
            throw new WebApplicationException(
                    Response.status(404).entity("{\"error\": \"No METAR data found for airport: " + code + ". Please verify the ICAO code.\"}").build());
        }

        AviationWeatherResponse response = responses.get(0);
        String rawMetar = response.getRawOb();

        if (rawMetar == null || rawMetar.trim().isEmpty()) {
            throw new WebApplicationException(
                    Response.status(404).entity("{\"error\": \"METAR data is empty for: " + code + "\"}").build());
        }

        return decoder.decode(rawMetar, response.getName());
    }
}
