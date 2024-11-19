package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.entity.Person;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonDao extends BasedDao<Person>{
    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(em.find(Person.class, id));
    }

    @Override
    public List<Person> getAll() {
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        return query.getResultList();
    }
}