package ru.shchetinin.lab1p.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import ru.shchetinin.lab1p.entity.Movie;
import ru.shchetinin.lab1p.entity.MovieGenre;
import ru.shchetinin.lab1p.entity.Person;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MovieDao extends BasedDao<Movie> {

    @PersistenceContext
    protected EntityManager em;

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

    // 1. Рассчитать среднее значение поля goldenPalmCount для всех объектов
    public Double calculateAverageGoldenPalmCount() {
        String jpql = "SELECT AVG(m.goldenPalmCount) FROM Movie m";
        return em.createQuery(jpql, Double.class).getSingleResult();
    }

    // 2. Сгруппировать объекты по значению поля name, вернуть количество элементов в каждой группе
    public List<Object[]> groupByNameAndCount() {
        String jpql = "SELECT m.name, COUNT(m) FROM Movie m GROUP BY m.name";
        return em.createQuery(jpql, Object[].class).getResultList();
    }

    // 3. Вернуть количество объектов, значение поля genre которых меньше заданного
    public Long countByGenreLessThan(String genre) {
        var mgenre = MovieGenre.valueOf(genre);
        String jpql = "SELECT COUNT(m) FROM Movie m WHERE m.genre = :genre";
        return em.createQuery(jpql, Long.class)
                .setParameter("genre", mgenre)
                .getSingleResult();
    }

    // 4. Получить список режиссёров, ни один фильм которых не получил ни одного "Оскара"
    public List<String> findDirectorsWithNoOscars() {
        String jpql = "SELECT DISTINCT d.name FROM Person d " +
                "WHERE NOT EXISTS (SELECT m FROM Movie m WHERE m.director = d AND m.oscarsCount > 0)";
        return em.createQuery(jpql, String.class).getResultList();
    }

    // 5. Равномерно перераспределить все "Оскары", полученные фильмами одного жанра, между фильмами другого жанра
    @Transactional
    public void redistributeOscars(String fromGenre, String toGenre) {
        var transaction = em.getTransaction();
        var fgenre = MovieGenre.valueOf(fromGenre);
        var tgenre = MovieGenre.valueOf(toGenre);
        String totalOscarsQuery = "SELECT SUM(m.oscarsCount) FROM Movie m WHERE m.genre = :fromGenre";
        Long totalOscars = em.createQuery(totalOscarsQuery, Long.class)
                .setParameter("fromGenre", fgenre)
                .getSingleResult();

        if (totalOscars == null || totalOscars == 0) {
            return;
        }

        String targetMoviesQuery = "SELECT m FROM Movie m WHERE m.genre = :toGenre";
        List<Movie> targetMovies = em.createQuery(targetMoviesQuery, Movie.class)
                .setParameter("toGenre", tgenre)
                .getResultList();

        if (targetMovies.isEmpty()) {
            return;
        }

        int moviesCount = targetMovies.size();
        int oscarsPerMovie = (int) (totalOscars / moviesCount);
        int remainingOscars = (int) (totalOscars % moviesCount);

        for (Movie movie : targetMovies) {
            movie.setOscarsCount(movie.getOscarsCount() + oscarsPerMovie);
        }

        if (!targetMovies.isEmpty() && remainingOscars > 0) {
            Movie firstMovie = targetMovies.get(0);
            firstMovie.setOscarsCount(firstMovie.getOscarsCount() + remainingOscars);
        }

        String clearOscarsQuery = "UPDATE Movie m SET m.oscarsCount = 0 WHERE m.genre = :fromGenre";
        em.createQuery(clearOscarsQuery)
                .setParameter("fromGenre", fgenre)
                .executeUpdate();

        transaction.commit();
    }

}
