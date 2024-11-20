-- Создание таблицы для Coordinates
CREATE TABLE IF NOT EXISTS coordinates (
                             id SERIAL PRIMARY KEY,  -- Автоматическое увеличение и уникальность
                             x BIGINT NOT NULL,
                             y FLOAT NOT NULL
);

-- Создание таблицы для Location
CREATE TABLE IF NOT EXISTS location (
                          id SERIAL PRIMARY KEY,  -- Автоматическое увеличение и уникальность
                          x DOUBLE PRECISION NOT NULL,
                          y BIGINT NOT NULL,
                          z FLOAT NOT NULL
);

-- Создание таблицы для Person
CREATE TABLE IF NOT EXISTS person (
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
CREATE TABLE IF NOT EXISTS movie (
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
                       tag_line VARCHAR(172),
                       genre VARCHAR(50),
                       FOREIGN KEY (coordinates_id) REFERENCES coordinates(id),
                       FOREIGN KEY (director_id) REFERENCES person(id),
                       FOREIGN KEY (screenwriter_id) REFERENCES person(id),
                       FOREIGN KEY (operator_id) REFERENCES person(id)
);


