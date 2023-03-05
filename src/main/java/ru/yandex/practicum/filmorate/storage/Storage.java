package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    T addObject(T object);

    T renewalObject(T object);

    void deleteObject(long id);

    T findObjectById(long id);

    List<T> getAllObjects();
}