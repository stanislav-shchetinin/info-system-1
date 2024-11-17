package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.service.MovieService;

import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MovieController {

    @Inject
    private MovieService movieService;

    @POST
    public Response createMovie(@Valid Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return Response.status(Response.Status.CREATED).entity(createdMovie).build();
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") Long id) {
        Movie movie = movieService.getMovieById(id);
        return Response.ok(movie).build();
    }

    @GET
    @Path("/all")
    public Response getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return Response.ok(movies).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") Long id, @Valid Movie updatedMovie) {
        Movie movie = movieService.updateMovie(id, updatedMovie);
        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") Long id) {
        movieService.deleteMovie(id);
        return Response.noContent().build();
    }
}
