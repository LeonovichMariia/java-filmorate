package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "memory")
public class InMemoryUserStorage extends AbstractStorage<User> implements UserStorage {
    public InMemoryUserStorage(){
        log.debug("Работаем в памяти");
    }
}