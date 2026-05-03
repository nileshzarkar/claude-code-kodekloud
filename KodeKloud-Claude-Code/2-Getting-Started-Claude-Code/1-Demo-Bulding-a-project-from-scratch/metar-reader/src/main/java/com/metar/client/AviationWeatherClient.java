package com.metar.client;

import com.metar.model.AviationWeatherResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api/data")
@RegisterRestClient(configKey = "aviation-weather")
public interface AviationWeatherClient {

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
