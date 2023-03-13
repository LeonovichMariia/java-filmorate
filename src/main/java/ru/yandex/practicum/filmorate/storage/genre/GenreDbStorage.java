package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(long id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        Genre genre = jdbcTemplate.queryForObject(sql, new GenreMapper(), id);
        if (genre == null) {
            log.warn(LogMessages.NULL_OBJECT.toString());
            throw new ObjectNotFoundException("Неизвестный жанр");
        }
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, new GenreMapper());
    }
}