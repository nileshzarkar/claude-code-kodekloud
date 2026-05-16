package com.greeting.resource;

import com.greeting.service.GreetingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/api/greet")
public class GreetingResource {

    @Inject
    GreetingService greetingService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response greet(Map<String, String> body) {
        String name = body.get("name");
        if (name == null || name.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Name must not be empty"))
                    .build();
        }
        String message = greetingService.greet(name);
        return Response.ok(Map.of("message", message)).build();
    }
}
