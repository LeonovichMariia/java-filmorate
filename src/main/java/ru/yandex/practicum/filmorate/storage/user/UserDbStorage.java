package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addObject(User object) {
        String sql = "INSERT INTO user_data (email, login, name, birthday) \n" +
                "VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement psst = connection.prepareStatement(sql, new String[]{"user_id"});
            psst.setString(1, object.getEmail());
            psst.setString(2, object.getLogin());
            psst.setString(3, object.getName());
            psst.setDate(4, Date.valueOf(object.getBirthday()));
            return psst;
        }, keyHolder);
        object.setId(keyHolder.getKey().longValue());
        log.info(LogMessages.OBJECT_ADDED.toString(), object);
        return object;
    }

    @Override
    public User renewalObject(User object) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int userData = jdbcTemplate.update(sql, object.getEmail(), object.getLogin(), object.getName(),
                object.getBirthday(), object.getId());
        if (userData == 0) {
            log.warn(LogMessages.OBJECT_NOT_FOUND.toString());
            throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString());
        }
        return object;
    }

    @Override
    public void deleteObject(long id) {
        String sql = "DELETE FROM friend \n" +
                "WHERE user_id = ?\n" +
                "AND friend_id = ? ";
        jdbcTemplate.update(sql, id, id);
    }

    @Override
    public User findObjectById(long id) {
        String sql = "SELECT * \n" +
                "FROM user_data\n" +
                "WHERE user_data.user_id = ?;";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
    }

    @Override
    public List<User> getAllObjects() {
        String sql = "SELECT * FROM user_data;";
        return jdbcTemplate.query(sql, new UserMapper());
    }
}