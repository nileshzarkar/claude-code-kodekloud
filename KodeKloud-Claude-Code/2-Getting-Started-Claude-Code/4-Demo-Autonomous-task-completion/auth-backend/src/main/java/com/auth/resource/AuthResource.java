package com.auth.resource;

import com.auth.dto.ErrorResponse;
import com.auth.dto.LoginRequest;
import com.auth.dto.RegisterRequest;
import com.auth.service.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        if (request == null) {
            return bad("Request body is required");
        }
        try {
            return Response.status(201)
                    .entity(authService.register(request.email, request.password))
                    .build();
        } catch (AuthService.ValidationException e) {
            return bad(e.getMessage());
        }
    }

    @GET
    @Path("/verify-email")
    public Response verifyEmail(@QueryParam("token") String token) {
        try {
            return Response.ok(authService.verifyEmail(token)).build();
        } catch (AuthService.ValidationException e) {
            return bad(e.getMessage());
        }
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        if (request == null) {
            return bad("Request body is required");
        }
        try {
            return Response.ok(authService.login(request.email, request.password)).build();
        } catch (AuthService.ValidationException e) {
            return bad(e.getMessage());
        } catch (AuthService.AuthException e) {
            return unauthorized(e.getMessage());
        }
    }

    @GET
    @Path("/me")
    public Response me(@Context ContainerRequestContext requestContext) {
        Long userId = (Long) requestContext.getProperty("userId");
        if (userId == null) {
            return unauthorized("Not authenticated");
        }
        try {
            return Response.ok(authService.getMe(userId)).build();
        } catch (AuthService.AuthException e) {
            return unauthorized(e.getMessage());
        }
    }

    // ---- helpers ----

    private Response bad(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(message)).build();
    }

    private Response unauthorized(String message) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse(message)).build();
    }
}
