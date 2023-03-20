package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
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
    public Map<Long, List<Genre>> getGenresForFilms() {
        String sql = "SELECT g.*, \n" +
                "fg.film_id  \n" +
                "FROM genre AS g, film_genre fg \n" +
                "WHERE g.genre_id = fg.genre_id";
        return jdbcTemplate.query(sql,
                rs -> {
                    Map<Long, List<Genre>> filmList = new HashMap<>();
                    while (rs.next()) {
                        long filmId = rs.getLong("film_id");
                        List<Genre> genreList = new ArrayList<>();
                        genreList.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
                        filmList.put(filmId, genreList);
                    }
                    return filmList;
                });
    }
}