package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends AbstractController<Film> {
    private static final LocalDate BIRTH_DATE_OF_CINEMA = LocalDate.of(1895, 12, 28);

    @GetMapping
    @Override
    public List<Film> getAll() {
        return super.getAll();
    }

    @PostMapping
    @Override
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        return super.add(film);
    }

    @PutMapping
    @Override
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return super.update(film);
    }

    public Film validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BIRTH_DATE_OF_CINEMA)) {
            log.info("Дата релиза не может быть раньше 28.12.1895");
            throw new ValidationException("Некорректная дата релиза!");
        }
        return film;
    }
}