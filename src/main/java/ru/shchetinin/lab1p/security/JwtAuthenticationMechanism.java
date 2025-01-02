package ru.shchetinin.lab1p.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Principal;
import java.util.Optional;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@Provider
@Priority(Priorities.AUTHENTICATION)
@JWT
public class JwtAuthenticationMechanism implements ContainerRequestFilter {

    @Inject
    private PostgresIdentityStoreHandler identityStoreHandler;

    @Inject
    private JwtUtil jwtUtil;

    @Override
    public void filter(ContainerRequestContext crc) {
        Optional<String> optionalAccessToken = extractAccessToken(crc);

        if (optionalAccessToken.isEmpty()) {

            crc.abortWith(
                    Response.status(UNAUTHORIZED)
                            .build()
            );

            return;
        }

        String accessToken = optionalAccessToken.get();

        String username;

        try {
            username = jwtUtil.validateTokenAndRetrieveClaim(accessToken);
        } catch (JWTVerificationException e) {

            crc.abortWith(
                    Response.status(UNAUTHORIZED)
                            .build()
            );

            return;
        }

        var result = identityStoreHandler.validate(
                new UsernamePasswordCredential(username, "")
        );

        if (!result.isAuthenticated()) {

            crc.abortWith(
                    Response.status(UNAUTHORIZED)
                            .build()
            );

            return;
        }

        crc.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> username;
            }

            @Override
            public boolean isUserInRole(String role) {
                return result.getRoles().contains(role);
            }

            @Override
            public boolean isSecure() {
                return crc.getUriInfo().getAbsolutePath().toString().startsWith("https");
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });

    }

    private Optional<String> extractAccessToken(ContainerRequestContext crc) {
        String authHeader = crc.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return Optional.of(authHeader.split(" ")[1].trim());
        }

        return Optional.empty();
    }

}
