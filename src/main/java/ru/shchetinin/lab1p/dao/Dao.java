package ru.shchetinin.lab1p.dao;

import ru.shchetinin.lab1p.entity.Coordinates;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    void save(T t);
    Optional<T> findById(Long id);
    List<T> getAll();
    void update(T t);
    boolean delete(Long id);
}
