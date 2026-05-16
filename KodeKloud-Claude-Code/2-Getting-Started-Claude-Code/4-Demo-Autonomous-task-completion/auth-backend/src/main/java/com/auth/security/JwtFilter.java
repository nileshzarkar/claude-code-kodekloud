package com.auth.security;

import com.auth.dto.ErrorResponse;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Inject
    JwtUtil jwtUtil;

    private static final String PROTECTED_PATH = "/api/auth/me";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if (!path.equals(PROTECTED_PATH)) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            abort(requestContext, "Authorization header is missing or invalid. Use: Bearer <token>");
            return;
        }

        String token = authHeader.substring(7).trim();

        try {
            Claims claims = jwtUtil.parseToken(token);
            // Store claims as request properties for the resource to read
            Long userId = ((Number) claims.get("userId")).longValue();
            requestContext.setProperty("userId", userId);
            requestContext.setProperty("email", claims.getSubject());
        } catch (Exception e) {
            abort(requestContext, "Invalid or expired token");
        }
    }

    private void abort(ContainerRequestContext ctx, String message) {
        ctx.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(new ErrorResponse(message))
                    .build()
        );
    }
}
