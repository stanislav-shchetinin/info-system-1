package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import ru.shchetinin.lab1p.entity.Movie;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieService {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    // Метод для создания нового фильма
    public Movie createMovie(Movie movie) {
        try {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();
            return movie;
        } finally {
            em.close();
        }
    }

    // Метод для получения фильма по ID
    public Optional<Movie> getMovieById(int id) {
        try {
            return Optional.ofNullable(em.find(Movie.class, id));
        } finally {
            em.close();
        }
    }

    // Метод для обновления существующего фильма
    public Movie updateMovie(int id, Movie updatedMovie) {
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) {
                // Обновляем все поля
                movie.setName(updatedMovie.getName());
                movie.setCoordinates(updatedMovie.getCoordinates());
                movie.setOscarsCount(updatedMovie.getOscarsCount());
                movie.setBudget(updatedMovie.getBudget());
                movie.setTotalBoxOffice(updatedMovie.getTotalBoxOffice());
                movie.setMpaaRating(updatedMovie.getMpaaRating());
                movie.setDirector(updatedMovie.getDirector());
                movie.setScreenwriter(updatedMovie.getScreenwriter());
                movie.setOperator(updatedMovie.getOperator());
                movie.setLength(updatedMovie.getLength());
                movie.setGoldenPalmCount(updatedMovie.getGoldenPalmCount());
                movie.setUsaBoxOffice(updatedMovie.getUsaBoxOffice());
                movie.setTagline(updatedMovie.getTagline());
                movie.setGenre(updatedMovie.getGenre());

                em.merge(movie);
            }
            em.getTransaction().commit();
            return movie;
        } finally {
            em.close();
        }
    }

    // Метод для удаления фильма по ID
    public boolean deleteMovie(int id) {
        try {
            em.getTransaction().begin();
            Movie movie = em.find(Movie.class, id);
            if (movie != null) {
                em.remove(movie);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } finally {
            em.close();
        }
    }

    // Метод для получения всех фильмов
    public List<Movie> getAllMovies() {
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
