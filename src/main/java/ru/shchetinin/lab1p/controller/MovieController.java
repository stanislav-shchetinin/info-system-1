package ru.shchetinin.lab1p.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import ru.shchetinin.lab1p.dao.MovieDao;
import ru.shchetinin.lab1p.dto.request.MovieRequest;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.security.JWT;
import ru.shchetinin.lab1p.service.MovieService;

import java.util.List;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class MovieController {

    @Inject
    private MovieService movieService;

    @Inject
    private MovieDao movieDao;

    @Inject
    private SecurityContext securityContext;

    @POST
    @JWT
    public Response createMovie(@Valid MovieRequest movie) {
        String username = securityContext.getUserPrincipal().getName();
        Movie createdMovie = movieService.createMovie(movie, username);
        return Response.status(Response.Status.CREATED).entity(createdMovie).build();
    }

    @GET
    @Path("/{id}")
    @JWT
    public Response getMovieById(@PathParam("id") Long id) {
        Movie movie = movieService.getMovieById(id);
        return Response.ok(movie).build();
    }

    @GET
    @Path("/all")
    @JWT
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
    @JWT
    public Response updateMovie(@PathParam("id") Long id, @Valid MovieRequest updatedMovie) {
        String username = securityContext.getUserPrincipal().getName();
        Movie movie = movieService.updateMovie(id, updatedMovie, username);
        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    @JWT
    public Response deleteMovie(@PathParam("id") Long id) {
        String username = securityContext.getUserPrincipal().getName();
        movieService.deleteMovie(id, username);
        return Response.noContent().build();
    }

    // 1. Рассчитать среднее значение поля goldenPalmCount для всех объектов
    @GET
    @Path("/average-golden-palm")
    public Double getAverageGoldenPalmCount() {
        return movieDao.calculateAverageGoldenPalmCount();
    }

    // 2. Сгруппировать объекты по значению поля name, вернуть количество элементов в каждой группе
    @GET
    @Path("/group-by-name")
    public List<Object[]> getGroupByNameAndCount() {
        return movieDao.groupByNameAndCount();
    }

    // 3. Вернуть количество объектов, значение поля genre которых меньше заданного
    @GET
    @Path("/count-by-genre")
    public Long getCountByGenreLessThan(@QueryParam("genre") String genre) {
        return movieDao.countByGenreLessThan(genre);
    }

    // 4. Получить список режиссёров, ни один фильм которых не получил ни одного "Оскара"
    @GET
    @Path("/directors-no-oscars")
    public List<String> getDirectorsWithNoOscars() {
        return movieDao.findDirectorsWithNoOscars();
    }

    // 5. Равномерно перераспределить "Оскары" между жанрами
    @POST
    @Path("/redistribute-oscars")
    public void redistributeOscars(
            @QueryParam("fromGenre") String fromGenre,
            @QueryParam("toGenre") String toGenre) {
        movieDao.redistributeOscars(fromGenre, toGenre);
    }
}
