package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.core.SecurityContext;
import ru.shchetinin.lab1p.dto.response.ApplicationDTO;
import ru.shchetinin.lab1p.excepion.EndpointException;
import ru.shchetinin.lab1p.security.JWT;
import ru.shchetinin.lab1p.security.SecurityManager;
import ru.shchetinin.lab1p.service.AdminService;

import java.util.List;

import static jakarta.ws.rs.core.Response.Status.*;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static ru.shchetinin.lab1p.entity.Role.ADMIN;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AdminController {

    @Inject
    SecurityContext securityContext;

    @Inject
    private SecurityManager securityManager;

    @Inject
    private AdminService adminService;

    @POST
    @JWT
    public Response submitApplication() {
        String username = securityContext.getUserPrincipal().getName();
        if (securityManager.hasAnyRole(String.valueOf(ADMIN))) {
            throw new EndpointException(
                    BAD_REQUEST,
                    "You are already an administrator"
            );
        }

        adminService.submitApplication(username);

        return Response.status(CREATED)
                .build();
    }

    @PUT
    @JWT
    public Response acceptApplication(ApplicationDTO applicationDTO) {
        if (!securityManager.hasAnyRole(String.valueOf(ADMIN))) {
            securityManager.throwForbiddenException();
        }

        adminService.acceptApplication(applicationDTO);

        return Response.status(CREATED)
                .build();
    }

    @GET
    @JWT
    public Response getApplications(@QueryParam("page") @DefaultValue("1") int page,
                                    @QueryParam("size") @DefaultValue("10") int size){
        if (!securityManager.hasAnyRole(String.valueOf(ADMIN))) {
            securityManager.throwForbiddenException();
        }

        List<ApplicationDTO> responses = adminService.getAllApplications(page, size);

        return Response.ok(responses)
                .build();
    }

}
