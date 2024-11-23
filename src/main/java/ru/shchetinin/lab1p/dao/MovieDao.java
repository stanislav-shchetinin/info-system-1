package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.entity.Person;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieDao extends BasedDao<Movie> {

    @Override
    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(em.find(Movie.class, id));
    }

    @Override
    public List<Movie> getAll(int page, int size) {
        return em.createQuery("SELECT m FROM Movie m", Movie.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

    }

}
