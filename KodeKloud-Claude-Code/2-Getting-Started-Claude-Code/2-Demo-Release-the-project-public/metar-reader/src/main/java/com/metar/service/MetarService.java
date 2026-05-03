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

/**
 * Application service that orchestrates METAR retrieval and decoding.
 * Validates the incoming airport code, calls the Aviation Weather API via
 * {@link AviationWeatherClient}, and delegates raw METAR parsing to {@link MetarDecoder}.
 */
@ApplicationScoped
public class MetarService {

    @RestClient
    AviationWeatherClient aviationWeatherClient;

    @Inject
    MetarDecoder decoder;

    /**
     * Fetches and decodes the latest METAR report for the given ICAO airport code.
     * <p>
     * Validates that the code is 3–4 characters, calls the external Aviation Weather API,
     * and returns a fully decoded {@link DecodedMetar} object. Throws
     * {@link WebApplicationException} with appropriate HTTP status codes on input errors,
     * upstream API failures, or missing data.
     *
     * @param airportCode ICAO airport code (3–4 uppercase characters, e.g. "KLAX")
     * @return decoded METAR as a populated {@link DecodedMetar} object
     * @throws WebApplicationException HTTP 400 if the code length is invalid,
     *                                 HTTP 503 if the Aviation Weather API is unreachable,
     *                                 HTTP 404 if no METAR data exists for the requested station
     */
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
