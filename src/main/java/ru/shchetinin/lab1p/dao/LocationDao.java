package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Location;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LocationDao extends BasedDao<Location>{
    @Override
    public Optional<Location> findById(Long id) {
        return Optional.ofNullable(em.find(Location.class, id));
    }

    @Override
    public List<Location> getAll() {
        TypedQuery<Location> query = em.createQuery("SELECT l FROM Location l", Location.class);
        return query.getResultList();
    }
}
