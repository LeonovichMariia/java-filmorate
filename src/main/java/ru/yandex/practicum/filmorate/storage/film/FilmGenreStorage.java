package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenreStorage {
    List<Genre> getGenresForPopularFilms(int count);
    List<Genre> getGenresForAllFilms();
}