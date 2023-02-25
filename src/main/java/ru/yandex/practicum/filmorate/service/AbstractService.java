package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractObject;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Slf4j
public abstract class AbstractService<T extends AbstractObject> {
    protected Storage<T> storage;

    protected abstract void validate(T object) throws ValidationException;

    public T addObject(T object) {
        validate(object);
        return storage.addObject(object);
    }

    public T findObjectById(long id) {
        return storage.findObjectById(id);
    }

    public T renewalObject(T object) {
        validate(object);
        return storage.renewalObject(object);
    }

    public void deleteObjectById(long id) {
        storage.deleteObject(id);
    }

    public List<T> getAll() {
        return storage.getAllObjects();
    }
}