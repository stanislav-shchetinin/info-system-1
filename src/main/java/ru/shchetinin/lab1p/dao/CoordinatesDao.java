package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ru.shchetinin.lab1p.entity.Coordinates;
import ru.shchetinin.lab1p.entity.Movie;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CoordinatesDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(Coordinates coordinates) {
        em.persist(coordinates);
    }

    public Optional<Coordinates> findById(Long id) {
        return Optional.ofNullable(em.find(Coordinates.class, id));
    }

    public List<Coordinates> getAllMovies() {
        TypedQuery<Coordinates> query = em.createQuery("SELECT c FROM Coordinates c", Coordinates.class);
        return query.getResultList();
    }

    public void update(Coordinates coordinates) {
        em.merge(coordinates);
    }

    public boolean delete(Long id) {
        var coordinates = findById(id);
        if (coordinates.isEmpty()) return false;
        em.remove(coordinates);
        return true;
    }

}
