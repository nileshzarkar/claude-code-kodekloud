package com.metar.resource;

import com.metar.model.DecodedMetar;
import com.metar.service.MetarService;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/metar")
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "METAR", description = "Fetch and decode aviation weather reports")
public class MetarResource {

    @Inject
    MetarService metarService;

    @GET
    @Path("/{code}")
    @Blocking
    @Operation(summary = "Get decoded METAR", description = "Fetches and decodes a METAR report for the given ICAO airport code")
    public Response getMetar(@PathParam("code") String code) {
        if (code == null || code.isBlank() || code.length() < 3 || code.length() > 4) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Airport code must be 3-4 characters (e.g. KJFK, EGLL, VABB)\"}")
                    .build();
        }

        try {
            DecodedMetar metar = metarService.getMetar(code.trim());
            if (metar.error != null) {
                return Response.status(Response.Status.NOT_FOUND).entity(metar).build();
            }
            return Response.ok(metar).build();
        } catch (Exception e) {
            DecodedMetar errorResponse = new DecodedMetar();
            errorResponse.station = code.toUpperCase();
            errorResponse.error = "Failed to fetch METAR data. The aviation weather service may be unavailable. Please try again shortly.";
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(errorResponse).build();
        }
    }
}
