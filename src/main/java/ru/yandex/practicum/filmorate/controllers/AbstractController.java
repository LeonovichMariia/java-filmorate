package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractController<T extends Object> {
    private int id = 1;
    private final Map<Integer, T> allObjects = new HashMap<>();

    private int generateId() {
        return id++;
    }

    public List<T> getAll() {
        log.info(String.valueOf(LogMessages.TOTAL), allObjects.size());
        return new ArrayList<>(allObjects.values());
    }

    public T add(T t) throws ValidationException {
        validate(t);
        if (allObjects.containsKey(t.getId())) {
            log.info(String.valueOf(LogMessages.ALREADY_EXIST), t);
            throw new ObjectAlreadyExistException("Такой объект уже есть");
        }
        t.setId(generateId());
        allObjects.put(t.getId(), t);
        log.info(String.valueOf(LogMessages.OBJECT_ADD), t);
        return t;
    }

    public T update(T t) throws ValidationException {
        validate(t);
        if (allObjects.get(t.getId()) != null) {
            allObjects.replace(t.getId(), t);
            log.info(String.valueOf(LogMessages.OBJECT_UPDATE), t);
        } else {
            log.info(String.valueOf(LogMessages.OBJECT_NOT_FOUND), t);
            throw new ObjectNotFoundException("Объект не найден!");
        }
        return t;
    }

    public abstract T validate(T t) throws ValidationException;
}