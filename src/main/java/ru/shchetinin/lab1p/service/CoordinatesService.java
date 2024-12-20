package ru.shchetinin.lab1p.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.dao.CoordinatesDao;
import ru.shchetinin.lab1p.dto.request.CoordinatesRequest;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CoordinatesService {
    @Inject
    private CoordinatesDao coordinatesDao;

    @Inject
    private ModelMapper modelMapper;

    @Transactional
    public Coordinates createCoordinates(CoordinatesRequest coordinatesRequest) {
        Coordinates coordinates = modelMapper.map(coordinatesRequest, Coordinates.class);
        coordinatesDao.save(coordinates);
        return coordinates;
    }

    public Coordinates getCoordinateById(Long id) {
        var coordinates = coordinatesDao.findById(id);
        if (coordinates.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, "Coordinates not found");
        }
        return coordinates.get();
    }

    public List<Coordinates> getAllCoordinates(int page, int size) {
        return coordinatesDao.getAll(page, size);
    }

    @Transactional
    public Coordinates updateCoordinates(Long id, CoordinatesRequest coordinatesRequest) {
        Optional<Coordinates> coordinatesOptional = coordinatesDao.findById(id);
        if (coordinatesOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Coordinates with id %d not found", id));
        }
        var coordinates = coordinatesOptional.get();
        modelMapper.map(coordinatesRequest, coordinates);
        coordinatesDao.update(coordinates);

        return coordinates;
    }

    @Transactional
    public void deleteCoordinates(Long id) {
        if (!coordinatesDao.delete(id)) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Coordinates with id %d not found", id));
        }
    }

}
