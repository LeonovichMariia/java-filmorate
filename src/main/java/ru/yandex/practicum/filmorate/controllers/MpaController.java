package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService service;

    @GetMapping
    public List<Mpa> getAllMpas() {
        log.info(LogMessages.GET_MPA_REQUEST.toString());
        return service.getAllMpas();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        log.info(LogMessages.GET_MPA_BY_ID_REQUEST.toString(), id);
        return service.getMpaById(id);
    }
}