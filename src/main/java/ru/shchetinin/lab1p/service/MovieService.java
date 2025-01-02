package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dao.MovieDao;
import ru.shchetinin.lab1p.dao.UserDao;
import ru.shchetinin.lab1p.dto.request.MovieRequest;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.entity.Person;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.entity.Role;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class MovieService {
    @Inject
    private MovieDao movieDao;

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private PersonService personService;

    @Inject
    private CoordinatesService coordinatesService;

    @Inject
    private UserDao userDao;

    @Transactional
    public Movie createMovie(MovieRequest movieRequest, String username) {
        Movie movie = modelMapper.map(movieRequest, Movie.class);
        setDependencies(movieRequest, movie);
        movie.setCreationDate(new Date());

        var opt = userDao.findByUsername(username);
        opt.ifPresent(user -> movie.setCreator(user.getId()));

        movieDao.save(movie);

        return movie;
    }

    public Movie getMovieById(Long id) {
        var movie = movieDao.findById(id);
        if (movie.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, "Movie not found");
        }
        return movie.get();
    }

    public List<Movie> getAllMovies(int page, int size, String filterColumn,
                                    String filterValue, String sortColumn, boolean asc) {
        return movieDao.getAll(page, size, filterColumn, filterValue, sortColumn, asc);
    }

    @Transactional
    public Movie updateMovie(Long id, MovieRequest movieRequest, String username) {

        var opt = userDao.findByUsername(username);
        boolean isAuth = false;

        if (opt.isPresent()) {
            var creator = movieDao.findById(id).get().getCreator();
            isAuth = Objects.equals(creator, opt.get().getId()) || opt.get().getRole().equals(Role.ADMIN);
        }

        if (!isAuth) {
            throw new EndpointException(Response.Status.FORBIDDEN, String.format("Person with id %d is not creator", id));
        }

        Optional<Movie> movieOptional = movieDao.findById(id);
        if (movieOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Movie with id %d not found", id));
        }
        var movie = movieOptional.get();
        modelMapper.map(movieRequest, movie);
        setDependencies(movieRequest, movie);

        movieDao.update(movie);

        return movie;
    }

    @Transactional
    public void deleteMovie(Long id, String username) {

        var opt = userDao.findByUsername(username);
        boolean isAuth = false;

        if (opt.isPresent()) {
            var creator = movieDao.findById(id).get().getCreator();
            isAuth = Objects.equals(creator, opt.get().getId()) || opt.get().getRole().equals(Role.ADMIN);
        }

        if (!isAuth) {
            throw new EndpointException(Response.Status.FORBIDDEN, String.format("Person with id %d is not creator", id));
        }


        if(!movieDao.delete(id)) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Movie with id %d not found", id));
        }
    }

    private void setDependencies(MovieRequest movieRequest, Movie movie) {

        Coordinates coordinates = modelMapper.map(
                coordinatesService.getCoordinateById(movieRequest.getCoordinates()),
                Coordinates.class
        );

        movie.setCoordinates(coordinates);

        if (movieRequest.getDirector() != null) {
            Person director = modelMapper.map(
                    personService.getPersonById(movieRequest.getDirector()),
                    Person.class
            );

            movie.setDirector(director);
        }

        Person screenwriter = modelMapper.map(
                personService.getPersonById(movieRequest.getScreenwriter()),
                Person.class
        );

        movie.setScreenwriter(screenwriter);

        if (movieRequest.getOperator() != null) {
            Person operator = modelMapper.map(
                    personService.getPersonById(movieRequest.getOperator()),
                    Person.class
            );

            movie.setOperator(operator);
        }
    }


}
