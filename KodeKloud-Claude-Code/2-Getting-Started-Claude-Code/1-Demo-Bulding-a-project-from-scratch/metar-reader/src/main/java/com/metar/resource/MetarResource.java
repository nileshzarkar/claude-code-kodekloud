package com.metar.resource;

import com.metar.model.DecodedMetar;
import com.metar.service.MetarService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/metar")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "METAR", description = "Aviation weather METAR fetch and decode")
public class MetarResource {

    @Inject
    MetarService metarService;

    @GET
    @Path("/{airportCode}")
    @Operation(
            summary = "Decode METAR for an airport",
            description = "Fetches the latest METAR from aviationweather.gov and decodes it into human-readable components"
    )
    public Response getMetar(
            @Parameter(description = "ICAO airport code (e.g., KLAX, EGLL, VABB)", example = "KLAX")
            @PathParam("airportCode") String airportCode) {
        DecodedMetar metar = metarService.getMetar(airportCode);
        return Response.ok(metar).build();
    }

    @GET
    @Path("/health")
    @Operation(summary = "Health check")
    public Response health() {
        return Response.ok("{\"status\": \"UP\", \"service\": \"METAR Reader\"}").build();
    }
}
