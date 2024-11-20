package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dto.request.PersonRequest;
import ru.shchetinin.lab1p.entity.Person;
import ru.shchetinin.lab1p.service.PersonService;

import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PersonController {

    @Inject
    private PersonService personService;

    @POST
    public Response createPerson(@Valid PersonRequest person) {
        Person createdPerson = personService.createPerson(person);
        return Response.status(Response.Status.CREATED).entity(createdPerson).build();
    }

    @GET
    @Path("/{id}")
    public Response getPersonById(@PathParam("id") Long id) {
        Person person = personService.getPersonById(id);
        return Response.ok(person).build();
    }

    @GET
    @Path("/all")
    public Response getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return Response.ok(persons).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePerson(@PathParam("id") Long id, @Valid PersonRequest updatedPerson) {
        Person person = personService.updatePerson(id, updatedPerson);
        return Response.ok(person).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePerson(@PathParam("id") Long id) {
        personService.deletePerson(id);
        return Response.noContent().build();
    }
}
