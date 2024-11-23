package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dto.request.MovieRequest;
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
    public Response createMovie(@Valid MovieRequest movie) {
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
    public Response getAllMovies(@QueryParam("page") @DefaultValue("1") int page,
                                 @QueryParam("size") @DefaultValue("10") int size,
                                 @QueryParam("filterColumn") String filterColumn,
                                 @QueryParam("filterValue") String filterValue,
                                 @QueryParam("sortColumn") String sortColumn,
                                 @QueryParam("asc") @DefaultValue("true") boolean asc) {
        List<Movie> movies = movieService.getAllMovies(page, size, filterColumn,
                                                       filterValue, sortColumn, asc);
        return Response.ok(movies).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") Long id, @Valid MovieRequest updatedMovie) {
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
