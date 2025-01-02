package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ru.shchetinin.lab1p.entity.*;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserDao extends BasedDao<User>{

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public List<User> getAll(int page, int size) {
        return em.createQuery("SELECT u FROM User u", User.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public Optional<User> findByUsername(String username) {
        try {
            User user = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean isUserExist(String username) {
        return findByUsername(username).isPresent();
    }

    @Transactional
    public void setAdminRole(Long userId) {
        User u = findById(userId).get();
        u.setRole(Role.ADMIN);
        update(u);
    }

    public List<User> findAdmins() {
        return em.createQuery("FROM User u WHERE u.role = :role", User.class)
                .setParameter("role", Role.ADMIN)
                .getResultList();
    }
}

