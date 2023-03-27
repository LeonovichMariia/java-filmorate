package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmAudienceStorage {
    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getPopularFilmsList(int count);
}