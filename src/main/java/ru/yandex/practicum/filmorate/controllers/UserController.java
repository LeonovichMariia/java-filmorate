package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> allUsers = new HashMap<>();

    public int generateId() {
        return id++;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", allUsers.size());
        return new ArrayList<>(allUsers.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (allUsers.containsKey(user.getId())) {
            log.info("Такой пользователь {} уже есть", user);
            throw new ObjectAlreadyExistException("Такой пользователь уже есть");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.warn("Имя пользователя пустое. Использован логин");
        }
        user.setId(generateId());
        allUsers.put(user.getId(), user);
        log.info("Пользователь {} сохранен", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (allUsers.get(user.getId()) != null) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
                log.warn("Имя пользователя пустое. Использован логин");
            }
            allUsers.replace(user.getId(), user);
            log.info("Пользователь {} обновлен", user);
        } else {
            log.info("Пользователь {} не найден", user);
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
        return user;
    }
}