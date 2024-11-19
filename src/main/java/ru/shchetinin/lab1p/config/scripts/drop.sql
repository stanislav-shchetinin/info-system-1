-- Удаление внешних ключей и зависимостей
ALTER TABLE movie DROP CONSTRAINT IF EXISTS movie_coordinates_id_fkey;
ALTER TABLE movie DROP CONSTRAINT IF EXISTS movie_director_id_fkey;
ALTER TABLE movie DROP CONSTRAINT IF EXISTS movie_screenwriter_id_fkey;
ALTER TABLE movie DROP CONSTRAINT IF EXISTS movie_operator_id_fkey;
ALTER TABLE person DROP CONSTRAINT IF EXISTS person_location_id_fkey;

-- Удаление таблиц
DROP TABLE IF EXISTS movie CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS coordinates CASCADE;

-- Удаление типов (enums)
DROP TYPE IF EXISTS mpaa_rating;
DROP TYPE IF EXISTS movie_genre;
DROP TYPE IF EXISTS color;
DROP TYPE IF EXISTS country;
