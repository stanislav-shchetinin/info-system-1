package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.entity.Movie;
import org.example.service.MovieService;

import java.util.List;
import java.util.Optional;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieController {

    @Inject
    private MovieService movieService;

    // Создание нового фильма
    @POST
    public Response createMovie(Movie movie) {
        try {
            Movie createdMovie = movieService.createMovie(movie);
            return Response.status(Response.Status.CREATED).entity(createdMovie).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Ошибка при создании фильма").build();
        }
    }

    // Получение фильма по ID
    @GET
    @Path("/{id}")
    public Response getMovieById(@PathParam("id") int id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        if (movie.isPresent()) {
            return Response.ok(movie.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Фильм не найден").build();
        }
    }

    // Получение списка всех фильмов
    @GET
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    // Обновление фильма по ID
    @PUT
    @Path("/{id}")
    public Response updateMovie(@PathParam("id") int id, Movie updatedMovie) {
        try {
            Movie movie = movieService.updateMovie(id, updatedMovie);
            if (movie != null) {
                return Response.ok(movie).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Фильм не найден").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Ошибка при обновлении фильма").build();
        }
    }

    // Удаление фильма по ID
    @DELETE
    @Path("/{id}")
    public Response deleteMovie(@PathParam("id") int id) {
        boolean isDeleted = movieService.deleteMovie(id);
        if (isDeleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Фильм не найден").build();
        }
    }
}
