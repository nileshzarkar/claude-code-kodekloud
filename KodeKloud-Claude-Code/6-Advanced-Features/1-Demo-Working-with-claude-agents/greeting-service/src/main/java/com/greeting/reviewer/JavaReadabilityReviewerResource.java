package com.greeting.reviewer;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Map;

@Path("/api/review")
public class JavaReadabilityReviewerResource {

    @Inject
    JavaReadabilityReviewerService reviewerService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response review(Map<String, String> body) {
        String code = body.get("code");
        if (code == null || code.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Field 'code' must not be empty"))
                    .build();
        }
        String reviewResult = reviewerService.review(code);
        return Response.ok(Map.of("review", reviewResult)).build();
    }
}
