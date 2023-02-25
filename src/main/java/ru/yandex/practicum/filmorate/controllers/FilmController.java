package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info(LogMessages.GET_ALL_FILMS.toString());
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info(LogMessages.GET_FILM_BY_ID.toString(), id);
        return filmService.findObjectById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable long id) {
        log.info(LogMessages.DELETE_FILM_BY_ID.toString(), id);
        filmService.deleteObjectById(id);
        log.info(LogMessages.OBJECT_DELETED.toString(), id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        filmService.addObject(film);
        log.info(LogMessages.OBJECT_ADD.toString(), film);
        return film;
    }

    @PutMapping
    public Film filmRenewal(@Valid @RequestBody Film film) throws ValidationException {
        filmService.renewalObject(film);
        log.info(LogMessages.OBJECT_UPDATE.toString(), film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info(LogMessages.ADD_LIKE.toString(), id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info(LogMessages.REMOVE_LIKE.toString(), id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info(LogMessages.GET_POPULAR.toString());
        return filmService.getPopularFilmsList(count);
    }
}