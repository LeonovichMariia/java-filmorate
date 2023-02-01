package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ObjectAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private final Map<Integer, Film> allFilms = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}", allFilms.size());
        return new ArrayList<>(allFilms.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза не может быть раньше 28.12.1895");
            throw new ValidationException("Некорректная дата релиза!");
        }
        if (allFilms.containsKey(film.getId())) {
            log.info("Такой фильм {} уже есть", film);
            throw new ObjectAlreadyExistException("Такой фильм уже есть");
        }
        film.setId(generateId());
        allFilms.put(film.getId(), film);
        log.info("Фильм {} сохранен", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза не может быть раньше 28.12.1895");
            throw new ValidationException("Некорректная дата релиза!");
        }
        if (allFilms.get(film.getId()) != null) {
            allFilms.replace(film.getId(), film);
            log.info("Фильм {} обновлен", film);
        } else {
            log.info("Фильм {} не найден", film);
            throw new ObjectNotFoundException("Фильм не найден!");
        }
        return film;
    }

    public int generateId() {
        return id++;
    }
}