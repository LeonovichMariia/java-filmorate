package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public User addObject(User object) {
        return null;
    }

    @Override
    public User renewalObject(User object) {
        return null;
    }

    @Override
    public void deleteObject(long id) {

    }

    @Override
    public User findObjectById(long id) {
        String sql = "SELECT * \n" +
                "FROM user_data\n" +
                "WHERE user_data.user_id = ?;";
        User user = jdbcTemplate.queryForObject(sql, new UserMapper(), id);
        return user;
    }

    @Override
    public List<User> getAllObjects() {
        String sql = "SELECT * FROM user_data;";
        return jdbcTemplate.query(sql, new UserMapper());
    }
}
