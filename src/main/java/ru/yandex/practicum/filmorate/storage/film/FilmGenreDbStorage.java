package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenresForPopularFilms(int count) {
        final String sql = "SELECT DISTINCT fg.*  \n" +
                "FROM film_genre AS fg  \n" +
                "RIGHT JOIN (\n" +
                "SELECT fa.film_id\n" +
                "FROM film_audience AS fa  \n" +
                "GROUP BY fa.film_id \n" +
                "ORDER BY count(fa.film_id) DESC, fa.film_id ASC\n" +
                "LIMIT ?) AS fap ON fap.film_id = fg.film_id";
        return jdbcTemplate.query(sql, new GenreMapper(), count);
    }

    @Override
    public List<Genre> getGenresForAllFilms() {
        String sql = "SELECT *  FROM film_genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }
}