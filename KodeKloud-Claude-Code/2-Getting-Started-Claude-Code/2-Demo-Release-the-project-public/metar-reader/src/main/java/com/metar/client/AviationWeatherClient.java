package com.metar.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * REST client for the Aviation Weather Center API (aviationweather.gov).
 * Base URL and timeouts are configured via application.properties under the "aviation-weather" key.
 */
@RegisterRestClient(configKey = "aviation-weather")
public interface AviationWeatherClient {

    /**
     * Fetches a raw METAR report for the given airport.
     *
     * @param icaoCode ICAO airport code (e.g. KJFK, EGLL, VABB)
     * @param format   response format — pass "raw" to get the plain METAR string
     * @return raw METAR text as returned by the API
     */
    @GET
    @Path("/api/data/metar")
    @Produces(MediaType.WILDCARD)
    String getMetar(@QueryParam("ids") String icaoCode, @QueryParam("format") String format);
}
