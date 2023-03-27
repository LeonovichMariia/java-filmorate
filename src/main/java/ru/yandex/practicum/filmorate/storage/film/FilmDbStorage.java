package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film addObject(Film object) {
        String sql = "INSERT INTO film_data (name, description, release_date, duration, rate, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"film_id"});
            stmt.setString(1, object.getName());
            stmt.setString(2, object.getDescription());
            stmt.setDate(3, Date.valueOf(object.getReleaseDate()));
            stmt.setInt(4, object.getDuration());
            stmt.setLong(5, object.getRate());
            stmt.setLong(6, object.getMpa().getId());
            return stmt;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        saveGenresToFilm(object);
        log.info(LogMessages.OBJECT_ADDED.toString(), object);
        return object;
    }

    @Override
    public Film renewalObject(Film object) {
        String sql = "UPDATE film_data \n" +
                "SET name = ?, description = ?, release_date = ?, \n" +
                "duration = ?, rate = ?, mpa_id = ? \n" +
                "WHERE film_id = ?";
        int filmData = jdbcTemplate.update(sql,
                object.getName(),
                object.getDescription(),
                object.getReleaseDate(),
                object.getDuration(),
                object.getRate(),
                object.getMpa().getId(),
                object.getId());
        if (filmData == 0) {
            log.warn(LogMessages.OBJECT_NOT_FOUND.toString());
            throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString());
        }
        saveGenresToFilm(object);
        return object;
    }

    @Override
    public void deleteObject(long id) {
        String sql = "DELETE FROM film_data WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film findObjectById(long id) {
        try {
            String sql = "SELECT film_data.*, mpa.mpa_name \n" +
                    "FROM film_data\n" +
                    "INNER JOIN mpa ON film_data.mpa_id = mpa.mpa_id\n" +
                    "WHERE film_data.film_id = ?";
            Film film = jdbcTemplate.queryForObject(sql, new FilmMapper(), id);

            String sqlGetGenresForFilm = "SELECT * FROM film_genre INNER JOIN genre ON film_genre.genre_id=genre.genre_id WHERE film_id=?";

            LinkedHashSet<Genre> genre = new LinkedHashSet<>(jdbcTemplate.query(sqlGetGenresForFilm, new GenreMapper(), id));
            if (film != null) {
                film.setGenres(genre);
            } else {
                log.warn(LogMessages.OBJECT_NOT_FOUND.toString());
                throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString());
            }
            return film;
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString());
        }
    }

    @Override
    public List<Film> getAllObjects() {
        String getFilmSql = "SELECT film_data.*, mpa.mpa_name \n" +
                "FROM film_data, mpa\n" +
                "WHERE film_data.mpa_id = mpa.mpa_id\n" +
                "ORDER BY film_data.film_id";
        Map<Long, List<Genre>> genresForFilm = genreDbStorage.getGenresForFilms();
        return jdbcTemplate.query(getFilmSql, ((rs, rowNum) -> mapRow(rs, genresForFilm)));
    }

    private void saveGenresToFilm(Film film) {
        final long filmId = film.getId();
        Set<Genre> genres = film.getGenres();
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        if (genres == null || genres.isEmpty()) {
            return;
        }
        List<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(
                "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genreList.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return genreList.size();
                    }
                });
    }

    private Film mapRow(ResultSet rs, Map<Long, List<Genre>> filmsGenres) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getLong("rate"))
                .mpa(
                        Mpa.builder()
                                .id(rs.getLong("mpa_id"))
                                .name(rs.getString("mpa_name"))
                                .build()
                )
                .build();
        List<Genre> filmGenres = filmsGenres.getOrDefault(rs.getLong("film_id"), new ArrayList<>());
        film.setGenres(new LinkedHashSet<>(filmGenres));
        return film;
    }
}