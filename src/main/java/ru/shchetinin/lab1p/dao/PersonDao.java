package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonDao extends BasedDao<Person>{
    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(em.find(Person.class, id));
    }

    @Override
    public List<Person> getAll(int page, int size) {
        return em.createQuery("FROM Person", Person.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();

    }
}