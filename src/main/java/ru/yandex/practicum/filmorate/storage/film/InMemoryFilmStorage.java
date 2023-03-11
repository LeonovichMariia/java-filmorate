package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "memory")
public class InMemoryFilmStorage extends AbstractStorage<Film> implements FilmStorage {
    public InMemoryFilmStorage() {
        log.debug("Работаем в памяти");
    }
}