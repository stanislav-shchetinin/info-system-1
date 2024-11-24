package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.entity.Movie;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LocationDao extends BasedDao<Location>{
    @Override
    public Optional<Location> findById(Long id) {
        return Optional.ofNullable(em.find(Location.class, id));
    }

    @Override
    public List<Location> getAll(int page, int size) {
        return em.createQuery("SELECT l FROM Location l", Location.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

    }
}
