-- Создание таблицы "location"
CREATE TABLE IF NOT EXISTS location (
                          id BIGSERIAL PRIMARY KEY,
                          x DOUBLE PRECISION NOT NULL,
                          y BIGINT NOT NULL,
                          z FLOAT NOT NULL
);

-- Создание таблицы "coordinates"
CREATE TABLE IF NOT EXISTS coordinates (
                             id BIGSERIAL PRIMARY KEY,
                             x BIGINT NOT NULL,
                             y FLOAT NOT NULL
);

-- Создание таблицы "person"
CREATE TABLE IF NOT EXISTS person (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        eye_color VARCHAR(255) NOT NULL,
                        hair_color VARCHAR(255) NOT NULL,
                        location_id BIGINT NOT NULL,
                        birthday DATE NOT NULL,
                        nationality VARCHAR(255) NOT NULL,
                        CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES location(id)
);

-- Создание таблицы "movie"
CREATE TABLE IF NOT EXISTS movie (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       coordinates_id BIGINT NOT NULL,
                       oscars_count BIGINT NOT NULL CHECK (oscars_count >= 1),
                       budget DOUBLE PRECISION CHECK (budget >= 0),
                       total_box_office FLOAT CHECK (total_box_office >= 0),
                       mpaa_rating VARCHAR(255),
                       director_id BIGINT NOT NULL,
                       screenwriter_id BIGINT NOT NULL,
                       operator_id BIGINT NOT NULL,
                       length INT NOT NULL CHECK (length >= 1),
                       golden_palm_count INT CHECK (golden_palm_count >= 1),
                       tag_line VARCHAR(255) NOT NULL,
                       usa_box_office BIGINT NOT NULL CHECK (usa_box_office >= 1),
                       genre VARCHAR(255) NOT NULL,
                       CONSTRAINT fk_coordinates FOREIGN KEY (coordinates_id) REFERENCES coordinates(id),
                       CONSTRAINT fk_director FOREIGN KEY (director_id) REFERENCES person(id),
                       CONSTRAINT fk_screenwriter FOREIGN KEY (screenwriter_id) REFERENCES person(id),
                       CONSTRAINT fk_operator FOREIGN KEY (operator_id) REFERENCES person(id)
);

DO $$
    BEGIN
        -- Создание типа "color"
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'color') THEN
            CREATE TYPE color AS ENUM ('GREEN', 'BLUE', 'YELLOW', 'ORANGE');
        END IF;

        -- Создание типа "country"
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'country') THEN
            CREATE TYPE country AS ENUM ('UNITED_KINGDOM', 'NORTH_KOREA', 'JAPAN');
        END IF;

        -- Создание типа "movie_genre"
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'movie_genre') THEN
            CREATE TYPE movie_genre AS ENUM ('HORROR', 'FANTASY', 'SCIENCE_FICTION');
        END IF;

        -- Создание типа "mpaa_rating"
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'mpaa_rating') THEN
            CREATE TYPE mpaa_rating AS ENUM ('PG', 'PG_13', 'R', 'NC_17');
        END IF;
    END $$;

-- Изменение столбцов таблиц
ALTER TABLE person ALTER COLUMN eye_color TYPE color USING eye_color::color;
ALTER TABLE person ALTER COLUMN nationality TYPE country USING nationality::country;
ALTER TABLE movie ALTER COLUMN genre TYPE movie_genre USING genre::movie_genre;
ALTER TABLE movie ALTER COLUMN mpaa_rating TYPE mpaa_rating USING mpaa_rating::mpaa_rating;

