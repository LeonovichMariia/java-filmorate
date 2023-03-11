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
                "FROM films\n" +
                "INNER JOIN mpa ON films.mpa_id = mpa.mpa_id\n" +
                "WHERE films.film_id = ?;";
        Film film = jdbcTemplate.queryForObject(sql, new FilmMapper(), id);
        String genresSql = "SELECT *\n" +
                "FROM genres\n" +
                "INNER JOIN film_genre AS fg ON films.film_id = fg.film_id\n" +
                "INNER JOIN films ON fg.film_id = films.FILM_ID \n" +
                "WHERE films.film_id = ?;";
        List<Genre> genres = jdbcTemplate.query(genresSql, new GenresMapper(), id);
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
        return null;
    }
}