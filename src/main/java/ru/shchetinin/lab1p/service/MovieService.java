package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dao.MovieDao;
import ru.shchetinin.lab1p.dto.request.MovieRequest;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieService {
    @Inject
    private MovieDao movieDao;

    @Transactional
    public Movie createMovie(Movie movie) {
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

    public List<Movie> getAllMovies() {
        return movieDao.getAll();
    }

    @Transactional
    public Movie updateMovie(Long id, Movie updatedMovie) {
        Optional<Movie> movieOptional = movieDao.findById(id);
        if (movieOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Movie with id %d not found", id));
        }
        var movie = movieOptional.get();
        movie.setName(updatedMovie.getName());
        movie.setCoordinates(updatedMovie.getCoordinates());
        movie.setOscarsCount(updatedMovie.getOscarsCount());
        movie.setBudget(updatedMovie.getBudget());
        movie.setTotalBoxOffice(updatedMovie.getTotalBoxOffice());
        movie.setMpaaRating(updatedMovie.getMpaaRating());
        movie.setDirector(updatedMovie.getDirector());
        movie.setScreenwriter(updatedMovie.getScreenwriter());
        movie.setOperator(updatedMovie.getOperator());
        movie.setLength(updatedMovie.getLength());
        movie.setGoldenPalmCount(updatedMovie.getGoldenPalmCount());
        movie.setUsaBoxOffice(updatedMovie.getUsaBoxOffice());
        movie.setTagline(updatedMovie.getTagline());
        movie.setGenre(updatedMovie.getGenre());
        movieDao.update(movie);

        return movie;
    }

    @Transactional
    public void deleteMovie(Long id) {
        if(!movieDao.delete(id)) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Movie with id %d not found", id));
        }
    }

}
