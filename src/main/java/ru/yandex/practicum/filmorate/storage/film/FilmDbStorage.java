package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addObject(Film object) {
        return null;
    }

    @Override
    public Film renewalObject(Film object) {
        return null;
    }

    @Override
    public void deleteObject(long id) {

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
                "WHERE film_data.film_id = ?;";
        List<Genre> genres = jdbcTemplate.query(genresSql, new GenreMapper(), id);
        if (film != null) {
            film.setGenres(genres);
        } else {
            log.warn(LogMessages.NULL_OBJECT.toString());
            throw new ObjectNotFoundException("Неизвестный объект!");
        }
        return film;
    }

    @Override
    public List<Film> getAllObjects() {
        String sql = "SELECT * FROM film_data INNER JOIN mpa ON film_data.mpa_id = mpa.mpa_id INNER JOIN film_genre AS fg ON film_data.film_id = fg.film_id INNER JOIN film_data ON fg.film_id = film_data.film_id;";
        return jdbcTemplate.query(sql, new FilmMapper());
    }
}