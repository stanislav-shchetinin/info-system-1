package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dto.request.AuthenticationRequest;
import ru.shchetinin.lab1p.dto.response.AuthenticationResponse;
import ru.shchetinin.lab1p.service.AuthenticationService;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthenticationController {
    @Inject
    private AuthenticationService authenticationService;

    @POST
    public Response authenticate(AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        return Response.ok(response).build();
    }

}
