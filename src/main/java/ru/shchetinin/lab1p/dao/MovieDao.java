package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ru.shchetinin.lab1p.entity.Movie;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieDao {
    @PersistenceContext
    private EntityManager em;

    public void save(Movie movie) {
        var transaction = em.getTransaction();
        transaction.begin();
        em.persist(movie);
        transaction.commit();
    }

    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(em.find(Movie.class, id));
    }

    public List<Movie> getAllMovies() {
        TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
        return query.getResultList();
    }

    public void update(Movie movie) {
        em.merge(movie);
    }

    public boolean delete(Long id) {
        var movie = findById(id);
        if (movie.isEmpty()) return false;
        em.remove(movie);
        return true;
    }


}
