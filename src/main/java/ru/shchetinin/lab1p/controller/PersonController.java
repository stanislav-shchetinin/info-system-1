package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ru.shchetinin.lab1p.dto.request.PersonRequest;
import ru.shchetinin.lab1p.entity.Person;
import ru.shchetinin.lab1p.security.JWT;
import ru.shchetinin.lab1p.service.PersonService;

import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonController {

    @Inject
    private PersonService personService;

    @Inject
    private SecurityContext securityContext;

    @POST
    @JWT
    public Response createPerson(@Valid PersonRequest person) {
        String username = securityContext.getUserPrincipal().getName();
        Person createdPerson = personService.createPerson(person, username);
        return Response.status(Response.Status.CREATED).entity(createdPerson).build();
    }

    @GET
    @Path("/{id}")
    @JWT
    public Response getPersonById(@PathParam("id") Long id) {
        Person person = personService.getPersonById(id);
        return Response.ok(person).build();
    }

    @GET
    @Path("/all")
    @JWT
    public Response getAllPersons(@QueryParam("page") int page,
                                  @QueryParam("size") int size) {
        List<Person> persons = personService.getAllPersons(page, size);
        return Response.ok(persons).build();
    }

    @PUT
    @Path("/{id}")
    @JWT
    public Response updatePerson(@PathParam("id") Long id, @Valid PersonRequest updatedPerson) {
        String username = securityContext.getUserPrincipal().getName();
        Person person = personService.updatePerson(id, updatedPerson, username);
        return Response.ok(person).build();
    }

    @DELETE
    @Path("/{id}")
    @JWT
    public Response deletePerson(@PathParam("id") Long id) {
        String username = securityContext.getUserPrincipal().getName();
        personService.deletePerson(id, username);
        return Response.noContent().build();
    }
}
