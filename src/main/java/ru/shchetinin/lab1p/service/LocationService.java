package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.dao.LocationDao;
import ru.shchetinin.lab1p.dto.request.LocationRequest;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LocationService {

    @Inject
    private LocationDao locationDao;

    @Inject
    private ModelMapper modelMapper;

    @Transactional
    public Location createLocation(LocationRequest locationRequest) {
        Location location = modelMapper.map(locationRequest, Location.class);
        locationDao.save(location);
        return location;
    }

    public Location getLocationById(Long id) {
        Optional<Location> locationOptional = locationDao.findById(id);
        if (locationOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, "Location not found");
        }
        return locationOptional.get();
    }

    public List<Location> getAllLocations(int page, int size) {
        return locationDao.getAll(page, size);
    }

    @Transactional
    public Location updateLocation(Long id, LocationRequest locationRequest) {
        Optional<Location> locationOptional = locationDao.findById(id);
        if (locationOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Location with id %d not found", id));
        }

        var location = locationOptional.get();
        modelMapper.map(locationRequest, location);
        locationDao.update(location);

        return location;
    }

    @Transactional
    public void deleteLocation(Long id) {
        if (!locationDao.delete(id)) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Location with id %d not found", id));
        }
    }
}