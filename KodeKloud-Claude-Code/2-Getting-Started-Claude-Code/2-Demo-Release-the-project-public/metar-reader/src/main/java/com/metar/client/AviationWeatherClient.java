package com.metar.client;

import com.metar.model.AviationWeatherResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * Quarkus MicroProfile REST Client interface for the Aviation Weather API (aviationweather.gov).
 * Base URL and timeouts are configured via the "aviation-weather" key in application.properties.
 */
@Path("/api/data")
@RegisterRestClient(configKey = "aviation-weather")
public interface AviationWeatherClient {

    /**
     * Fetches METAR observations from aviationweather.gov for one or more ICAO station identifiers.
     *
     * @param ids    comma-separated ICAO station identifier(s), e.g. "KLAX" or "KLAX,KJFK"
     * @param format response format — use "json" for structured data
     * @param taf    whether to include TAF (Terminal Aerodrome Forecast) alongside the METAR
     * @param hours  number of past hours of data to retrieve (e.g. 2 returns the last 2 hours)
     * @return list of raw aviation weather responses for the requested station(s)
     */
    @GET
    @Path("/metar")
    @Produces(MediaType.APPLICATION_JSON)
    List<AviationWeatherResponse> getMetar(
            @QueryParam("ids") String ids,
            @QueryParam("format") String format,
            @QueryParam("taf") boolean taf,
            @QueryParam("hours") int hours
    );
}
