package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dto.request.UserRequest;
import ru.shchetinin.lab1p.entity.User;
import ru.shchetinin.lab1p.service.RegistrationService;

import static jakarta.ws.rs.core.Response.Status.CREATED;

@Path("/registration")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class RegistrationController {

    @Inject
    private RegistrationService registrationService;

    @POST
    public Response register(UserRequest userRequest) {
        User user = registrationService.register(userRequest);

        return Response.status(CREATED)
                .entity(user)
                .build();
    }


}
