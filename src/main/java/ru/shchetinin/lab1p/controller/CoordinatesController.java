package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    public Response createCoordinates(@Valid Coordinates coordinates) {
        var createdCoordinates = coordinatesService.createMovie(coordinates);
        return Response.status(Response.Status.CREATED).entity(createdCoordinates).build();
    }

    @GET
    @Path("/{id}")
    public Response getCoordinatesById(@PathParam("id") Long id) {
        var coordinates = coordinatesService.getMovieById(id);
        return Response.ok(coordinates).build();
    }

    @GET
    @Path("/all")
    public Response getAllCoordinates() {
        List<Coordinates> coordinates = coordinatesService.getAllMovies();
        return Response.ok(coordinates).build();
    }

}