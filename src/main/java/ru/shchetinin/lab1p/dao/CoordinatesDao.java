package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.entity.Movie;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CoordinatesDao extends BasedDao<Coordinates> {

    @Override
    public Optional<Coordinates> findById(Long id) {
        return Optional.ofNullable(em.find(Coordinates.class, id));
    }

    @Override
    public List<Coordinates> getAll(int page, int size) {
        return em.createQuery("FROM Coordinates", Coordinates.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

    }

}
