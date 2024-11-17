package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dao.CoordinatesDao;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.List;

@ApplicationScoped
public class CoordinatesService {
    @Inject
    private CoordinatesDao coordinatesDao;

    @Transactional
    public Coordinates createMovie(Coordinates coordinates) {
        coordinatesDao.save(coordinates);
        return coordinates;
    }

    public Coordinates getMovieById(Long id) {
        var coordinates = coordinatesDao.findById(id);
        if (coordinates.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, "Coordinates not found");
        }
        return coordinates.get();
    }

    public List<Coordinates> getAllMovies() {
        return coordinatesDao.getAllMovies();
    }

}
