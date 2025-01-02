package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import ru.shchetinin.lab1p.entity.Application;
import ru.shchetinin.lab1p.entity.Movie;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ApplicationDAO extends BasedDao<Application>{

    @Override
    public Optional<Application> findById(Long id) {
        return Optional.ofNullable(em.find(Application.class, id));
    }

    @Override
    public List<Application> getAll(int page, int size) {
        return em.createQuery("SELECT m FROM Application m", Application.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }
}
