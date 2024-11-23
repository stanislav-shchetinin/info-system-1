package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dto.request.CoordinatesRequest;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.service.CoordinatesService;
import ru.shchetinin.lab1p.service.MovieService;

import java.util.List;

@Path("/coordinates")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CoordinatesController {

    @Inject
    private CoordinatesService coordinatesService;

    @POST
    public Response createCoordinates(@Valid CoordinatesRequest coordinates) {
        var createdCoordinates = coordinatesService.createCoordinates(coordinates);
        return Response.status(Response.Status.CREATED).entity(createdCoordinates).build();
    }

    @GET
    @Path("/{id}")
    public Response getCoordinatesById(@PathParam("id") Long id) {
        var coordinates = coordinatesService.getCoordinateById(id);
        return Response.ok(coordinates).build();
    }

    @GET
    @Path("/all")
    public Response getAllCoordinates(@QueryParam("page") int page,
                                      @QueryParam("size") int size) {
        List<Coordinates> coordinates = coordinatesService.getAllCoordinates(page, size);
        return Response.ok(coordinates).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCoordinates(@PathParam("id") Long id, @Valid CoordinatesRequest updatedCoordinates) {
        Coordinates coordinates = coordinatesService.updateCoordinates(id, updatedCoordinates);
        return Response.ok(coordinates).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCoordinates(@PathParam("id") Long id) {
        coordinatesService.deleteCoordinates(id);
        return Response.noContent().build();
    }

}
