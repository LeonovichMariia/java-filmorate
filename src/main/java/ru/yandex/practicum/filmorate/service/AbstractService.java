package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.AbstractObject;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

@Slf4j
public abstract class AbstractService<T extends AbstractObject> {
    protected Storage<T> storage;

    protected abstract void validate(T object) throws ValidationException;

    protected void checkIfObjectNull(T object) {
        if (object == null) {
            log.warn(LogMessages.NULL_OBJECT.toString());
        }
    }

    public T addObject(T object) {
        validate(object);
        storage.addObject(object);
        log.info(LogMessages.OBJECT_ADDED.toString(), object);
        return object;
    }

    public T findObjectById(long id) {
        return storage.findObjectById(id);
    }

    public T renewalObject(T object) {
        validate(object);
        storage.renewalObject(object);
        log.info(LogMessages.OBJECT_UPDATED.toString(), object);
        return object;
    }

    public void deleteObjectById(long id) {
        storage.deleteObject(id);
        log.info(LogMessages.OBJECT_DELETED.toString(), id);
    }

    public List<T> getAll() {
        return storage.getAllObjects();
    }
}