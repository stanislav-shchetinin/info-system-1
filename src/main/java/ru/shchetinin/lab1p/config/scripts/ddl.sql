-- Создание таблицы для Coordinates
CREATE TABLE coordinates (
                             id SERIAL PRIMARY KEY,  -- Автоматическое увеличение и уникальность
                             x BIGINT NOT NULL,
                             y FLOAT NOT NULL
);

-- Создание таблицы для Location
CREATE TABLE location (
                          id SERIAL PRIMARY KEY,  -- Автоматическое увеличение и уникальность
                          x DOUBLE PRECISION NOT NULL,
                          y BIGINT NOT NULL,
                          z FLOAT NOT NULL
);

-- Создание таблицы для Person
CREATE TABLE person (
                        id SERIAL PRIMARY KEY,  -- Автоматическое увеличение и уникальность
                        name VARCHAR(255) NOT NULL,  -- Поле не может быть null
                        eye_color VARCHAR(50),
                        hair_color VARCHAR(50),
                        location_id INT,  -- Внешний ключ на Location
                        birthday DATE NOT NULL,  -- Поле не может быть null
                        nationality VARCHAR(50),
                        FOREIGN KEY (location_id) REFERENCES location(id)
);

-- Создание таблицы для Movie
CREATE TABLE movie (
                       id SERIAL PRIMARY KEY,  -- Автоматическое увеличение и уникальность
                       name VARCHAR(255) NOT NULL,  -- Поле не может быть null
                       coordinates_id INT,  -- Внешний ключ на Coordinates
                       creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- Поле не может быть null, генерируется автоматически
                       oscars_count BIGINT CHECK (oscars_count > 0),
                       budget DOUBLE PRECISION CHECK (budget > 0),
                       total_box_office DOUBLE PRECISION CHECK (total_box_office > 0),
                       mpaa_rating VARCHAR(10),
                       director_id INT,  -- Внешний ключ на Person
                       screenwriter_id INT,  -- Внешний ключ на Person
                       operator_id INT,  -- Внешний ключ на Person
                       length INT CHECK (length > 0),
                       golden_palm_count BIGINT CHECK (golden_palm_count > 0),
                       usa_box_office BIGINT CHECK (usa_box_office > 0),
                       tagline VARCHAR(172),
                       genre VARCHAR(50),
                       FOREIGN KEY (coordinates_id) REFERENCES coordinates(id),
                       FOREIGN KEY (director_id) REFERENCES person(id),
                       FOREIGN KEY (screenwriter_id) REFERENCES person(id),
                       FOREIGN KEY (operator_id) REFERENCES person(id)
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

