package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.storage.type", havingValue = "db")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sql, new MpaMapper(), id);
        if (mpa == null) {
            log.warn(LogMessages.NULL_OBJECT.toString());
            throw new ObjectNotFoundException("Неизвестный рейтинг");
        }
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpas() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, new MpaMapper());
    }
}