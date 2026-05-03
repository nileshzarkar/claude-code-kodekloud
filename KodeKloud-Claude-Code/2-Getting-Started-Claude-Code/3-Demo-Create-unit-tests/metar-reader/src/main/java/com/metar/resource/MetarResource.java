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

/**
 * JAX-RS REST resource that exposes the METAR API endpoints under {@code /api/metar}.
 * All responses are serialized as JSON. OpenAPI metadata is annotated for Swagger UI discovery.
 */
@Path("/api/metar")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "METAR", description = "Aviation weather METAR fetch and decode")
public class MetarResource {

    @Inject
    MetarService metarService;

    /**
     * Fetches and decodes the latest METAR report for the given ICAO airport code.
     * Delegates to {@link MetarService#getMetar(String)} and wraps the result in an HTTP 200 response.
     *
     * @param airportCode ICAO airport code path parameter (e.g., KLAX, EGLL, VABB)
     * @return HTTP 200 with decoded METAR JSON payload, or an error response if the code is
     *         invalid, the upstream API is unavailable, or no data exists for the station
     */
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

    /**
     * Returns a simple health check payload indicating the service is running.
     *
     * @return HTTP 200 with a JSON status object {@code {"status":"UP","service":"METAR Reader"}}
     */
    @GET
    @Path("/health")
    @Operation(summary = "Health check")
    public Response health() {
        return Response.ok("{\"status\": \"UP\", \"service\": \"METAR Reader\"}").build();
    }
}
