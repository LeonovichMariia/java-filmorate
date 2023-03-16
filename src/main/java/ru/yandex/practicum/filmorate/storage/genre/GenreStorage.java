package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Genre getGenreById(long id);

    Collection<Genre> getAllGenres();

    List<Genre> getGenreByFilmId(long filmId);
}