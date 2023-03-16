package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.film.FilmAudienceStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {
    private static final LocalDate BIRTH_DATE_OF_CINEMA = LocalDate.of(1895, 12, 28);
    public static final Comparator<Film> COMPARATOR = Comparator.comparing(Film::getRate).reversed();
    private final Storage<User> userStorage;
    private final FilmAudienceStorage filmAudienceStorage;

    @Autowired
    public FilmService(Storage<Film> filmStorage, Storage<User> userStorage, FilmAudienceStorage filmAudienceStorage) {
        log.info(filmStorage.getClass().toString());
        this.storage = filmStorage;
        this.userStorage = userStorage;
        this.filmAudienceStorage = filmAudienceStorage;
    }

    @Override
    public void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BIRTH_DATE_OF_CINEMA)) {
            log.warn(LogMessages.INCORRECT_FILM_RELEASE_DATE.toString());
            throw new ValidationException(LogMessages.INCORRECT_FILM_RELEASE_DATE.toString());
        }
    }

    public void addLike(long filmId, long userId) {
        Film film = storage.findObjectById(filmId);
        checkIfObjectNull(film);
        User user = userStorage.findObjectById(userId);
        if (user == null) {
            throw new ObjectNotFoundException(LogMessages.OBJECT_NOT_FOUND.toString() + userId);
        }
        filmAudienceStorage.addLike(filmId, userId);
        log.info(LogMessages.LIKED_FILM.toString(), film);
    }

    public void removeLike(long filmId, long userId) {
        Film film = storage.findObjectById(filmId);
        checkIfObjectNull(film);
        userStorage.findObjectById(userId);
        film.removeLike(userId);
        filmAudienceStorage.removeLike(filmId, userId);
        log.info(LogMessages.UNLIKED_FILM.toString(), film);
    }

    public List<Film> getPopularFilmsList(int count) {
        log.info(LogMessages.POPULAR_TOTAL.toString(), count);
        return filmAudienceStorage.getPopularFilmsList(count);
    }
}