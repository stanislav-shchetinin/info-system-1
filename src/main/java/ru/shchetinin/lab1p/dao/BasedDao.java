package ru.shchetinin.lab1p.dao;

import jakarta.decorator.Decorator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class BasedDao<T> implements Dao<T> {

    @PersistenceContext
    protected EntityManager em;

    @Override
    public void save(T t) {
        var transaction = em.getTransaction();
        transaction.begin();
        em.persist(t);
        transaction.commit();
    }

    @Override
    public void update(T t) {
        var transaction = em.getTransaction();
        transaction.begin();
        em.merge(t);
        transaction.commit();
    }

    @Override
    public boolean delete(Long id) {
        var t = findById(id);
        if (t.isEmpty()) return false;
        var transaction = em.getTransaction();
        transaction.begin();
        em.remove(t.get());
        transaction.commit();
        return true;
    }
}
