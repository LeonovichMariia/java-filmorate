DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS users(
user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
email varchar(300) NOT NULL UNIQUE,
login varchar(300) NOT NULL UNIQUE, 
name VARCHAR(300),
birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS films(
film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(300) NOT NULL UNIQUE,
description varchar(200)  DEFAULT 'не указано',
release_date DATE NOT NULL,
duration INTEGER NOT NULL,
mpa_id int
);

CREATE TABLE IF NOT EXISTS friends(
user_id int,
friend_id int,
friendship_status boolean DEFAULT false,
CONSTRAINT friends_pk PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS mpa(
mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(300) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_audience(
user_id INTEGER,
film_id INTEGER,
CONSTRAINT likes_pk PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS film_genre(
film_id INTEGER,
genre_id INTEGER,
CONSTRAINT film_genre_pk PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS genres(
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(300) NOT NULL UNIQUE
);

ALTER TABLE films ADD FOREIGN KEY (mpa_id) REFERENCES mpa(mpa_id) ON DELETE SET NULL;

ALTER TABLE friends ADD FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
ALTER TABLE friends ADD FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE;

ALTER TABLE film_audience ADD FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;
ALTER TABLE film_audience ADD FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE;

ALTER TABLE film_genre ADD FOREIGN KEY (film_id) REFERENCES films(film_id) ON DELETE CASCADE;
ALTER TABLE film_genre ADD FOREIGN KEY (genre_id) REFERENCES genres(genre_id) ON DELETE CASCADE;