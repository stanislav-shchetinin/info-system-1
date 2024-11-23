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

    public List<Movie> getAll(int page, int size, String filterColumn, String filterValue, String sortColumn, boolean asc) {
        StringBuilder queryBuilder = new StringBuilder("SELECT m FROM Movie m");

        boolean isValidFilter = filterColumn != null && !filterColumn.isEmpty() && filterValue != null && !filterValue.isEmpty();
        if (isValidFilter) {
            queryBuilder.append(" WHERE LOWER(CAST(m.").append(filterColumn).append(" AS string)) LIKE LOWER(:filterValue)");
        }

        if (sortColumn != null && !sortColumn.isEmpty()) {
            queryBuilder.append(" ORDER BY m.").append(sortColumn);
            queryBuilder.append(asc ? " ASC" : " DESC");
        }

        TypedQuery<Movie> query = em.createQuery(queryBuilder.toString(), Movie.class);

        if (isValidFilter) {
            query.setParameter("filterValue", "%" + filterValue + "%");
        }

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

}
