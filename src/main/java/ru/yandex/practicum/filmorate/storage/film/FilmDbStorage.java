package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addObject(Film object) {
        String sql = "INSERT INTO films(name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getDescription());
            stmt.setDate(3, Date.valueOf(object.getReleaseDate()));
            stmt.setLong(4, object.getDuration());
            stmt.setLong(5, object.getMpa().getId());
            return stmt;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        saveGenresToFilm(object);
        return object;
    }

    @Override
    public Film renewalObject(Film object) {
        String sql = "UPDATE film_data SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? where film_id = ?";
        jdbcTemplate.update(sql, object.getName(), object.getDescription(), object.getReleaseDate(),
                object.getDuration(), object.getMpa().getId(), object.getId());
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", object.getId());
        if (object.getGenres() != null && !object.getGenres().isEmpty()) {
            for (Genre genre : object.getGenres()) {
                String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
                jdbcTemplate.update(sqlGenre, object.getId(), genre.getId());
            }
        }
        return object;
    }

    @Override
    public void deleteObject(long id) {
        String sql = "DELETE FROM film_data WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film findObjectById(long id) {
        String sql = "SELECT * \n" +
                "FROM film_data\n" +
                "INNER JOIN mpa ON film_data.mpa_id = mpa.mpa_id\n" +
                "WHERE film_data.film_id = ?;";
        Film film = jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
        String genresSql = "SELECT *\n" +
                "FROM genre\n" +
                "INNER JOIN film_genre AS fg ON film_data.film_id = fg.film_id\n" +
                "INNER JOIN film_data ON fg.film_id = film_data.film_id \n" +
                "WHERE film_data.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(genresSql, new GenreMapper(), id);
        if (film != null) {
            film.setGenres(genres);
        } else {
            log.warn(LogMessages.NULL_OBJECT.toString());
            throw new ObjectNotFoundException(LogMessages.NULL_OBJECT.toString());
        }
        return film;
    }

    @Override
    public List<Film> getAllObjects() {
        String sql = "SELECT film_data.*,\n" +
                "mpa.name AS mpa,\n" +
                "genre.name AS genre \n" +
                "FROM film_data \n" +
                "INNER JOIN mpa ON film_data.mpa_id = mpa.mpa_id \n" +
                "INNER JOIN film_genre AS fg ON film_data.film_id = fg.film_id\n" +
                "INNER JOIN genre ON fg.genre_id = genre.genre_id";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

//    private boolean dbContainsFilm(Film film) {
//        String sql = "SELECT fd.*, mpa.name \n" +
//                "FROM film_data AS fd \n" +
//                "JOIN mpa ON fd.mpa_id = mpa.mpa_id \n" +
//                "WHERE fd.name = ? \n" +
//                "AND  fd.description = ?\n" +
//                "AND fd.release_date = ? \n" +
//                "AND fd.duration = ?\n" +
//                "AND fd.mpa_id = ?";
//        Film f = jdbcTemplate.queryForObject(sql, new FilmMapper(), film.getName(), film.getDescription(),
//                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
//        if (f == null) {
//            log.warn(LogMessages.NULL_OBJECT.toString());
//            return false;
//        } else {
//            return true;
//        }
//    }

    private void saveGenresToFilm(Film film) {
        long filmId = film.getId();
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        final List<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        final List<Genre> genreList = new ArrayList<>(genres);
        genreList.sort(Comparator.comparing(Genre::getId));
        film.getGenres().clear();
        for (Genre genre : genreList) {
            String sqlGenre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlGenre, filmId, genre.getId());
            film.addGenre(genre);
        }
    }
}