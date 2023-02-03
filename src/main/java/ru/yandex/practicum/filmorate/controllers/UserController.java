package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @GetMapping
    @Override
    public List<User> getAll() {
        return super.getAll();
    }

    @PostMapping
    @Override
    public User add(@Valid @RequestBody User user) {
        return super.add(user);
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        return super.update(user);
    }

    public User validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя пустое. Использован логин");
            user.setName(user.getLogin());
        }
        return user;
    }
}