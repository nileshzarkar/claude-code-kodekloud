package com.metar.service;

import com.metar.client.AviationWeatherClient;
import com.metar.decoder.MetarDecoder;
import com.metar.model.DecodedMetar;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class MetarService {

    @RestClient
    AviationWeatherClient aviationWeatherClient;

    @Inject
    MetarDecoder decoder;

    public DecodedMetar getMetar(String icaoCode) {
        String raw = aviationWeatherClient.getMetar(icaoCode.toUpperCase(), "raw");

        if (raw == null || raw.isBlank()) {
            DecodedMetar error = new DecodedMetar();
            error.station = icaoCode.toUpperCase();
            error.error = "No METAR data found for airport code: " + icaoCode.toUpperCase() +
                    ". Please verify the ICAO code (e.g. KJFK, EGLL, VABB).";
            return error;
        }

        // The API may return multiple lines; take the first non-empty one
        String rawMetar = raw.lines()
                .map(String::trim)
                .filter(l -> !l.isBlank())
                .findFirst()
                .orElse(raw.trim());

        return decoder.decode(rawMetar);
    }
}
