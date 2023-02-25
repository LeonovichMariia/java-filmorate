package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.AbstractObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractStorage<T extends AbstractObject> implements Storage<T> {
    private final Map<Long, T> allObjects = new HashMap<>();
    private long id = 1L;

    private long generateId() {
        return id++;
    }

    protected void validate(long id) {
        if (!allObjects.containsKey(id)) {
            log.info(LogMessages.OBJECT_NOT_FOUND.toString(), id);
            throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString());
        }
    }

    @Override
    public T addObject(T object) {
        if (allObjects.containsKey(object.getId())) {
            log.info(LogMessages.ALREADY_EXIST.toString(), object);
            throw new ObjectAlreadyExistException(LogMessages.ALREADY_EXIST.toString());
        }
        object.setId(generateId());
        allObjects.put(object.getId(), object);
        return object;
    }

    @Override
    public T renewalObject(T object) {
        validate(object.getId());
        allObjects.replace(object.getId(), object);
        return object;
    }

    @Override
    public void deleteObject(long id) {
        validate(id);
        T object = allObjects.get(id);
        allObjects.remove(object.getId());
    }

    @Override
    public T findObjectById(long id) {
        validate(id);
        return allObjects.get(id);
    }

    @Override
    public List<T> getAllObjects() {
        log.info(LogMessages.TOTAL.toString(), allObjects.size());
        return new ArrayList<>(allObjects.values());
    }
}