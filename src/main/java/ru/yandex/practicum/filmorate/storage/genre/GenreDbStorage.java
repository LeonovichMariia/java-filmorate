package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(long id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new GenreMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            log.warn(LogMessages.OBJECT_NOT_FOUND.toString(), id);
            throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genre ORDER BY genre_id";
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    @Override
    public List<Genre> getGenreByFilmId(long filmId) {
        final String sql = "SELECT g.* \n" +
                "FROM genre AS g \n" +
                "RIGHT JOIN film_genre AS fg ON g.genre_id = fg.genre_id \n" +
                "WHERE film_id = ? \n" +
                "ORDER BY genre_id";
        return jdbcTemplate.query(sql, new GenreMapper(), filmId);
    }
}