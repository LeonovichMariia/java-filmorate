package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addFilmWithEmptyName() throws Exception {
        Film film = Film.builder()
                .description("Film description")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void addFilmWithIncorrectDuration() throws Exception {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film description")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(-60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void addFilmWithIncorrectDescription() throws Exception {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film descriptionjfghsoufghosrugfhierugferigfuaigueiguierugeiaugheirugeirgeiurghierughierguheirugheirughirgheriugheirugheriugheriughrieugheriughriugherigherghiruehgierhgiuerhgiuerhgiurhgirugheriughrieugheirughiurghuirehgieurg")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void addFilmWithIncorrectReleaseData() throws Exception {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film description")
                .releaseDate(LocalDate.of(1894, 6, 30))
                .duration(60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ValidationException));
    }

    @Test
    public void addFilmWithNullReleaseData() throws Exception {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film description")
                .duration(60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void addFilmWithCorrectData() throws Exception {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film description")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(get("/films")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void addAndUpdateFilmWithCorrectData() throws Exception {
        Film film = Film.builder()
                .name("Titanic")
                .description("Film description")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(60)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        Film film2 = Film.builder()
                .id(1L)
                .name("HomeAlone")
                .description("HomeAlone description")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(60)
                .build();
        mockMvc.perform(put("/films").content(objectMapper.writeValueAsString(film2))
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/films")).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetFilms() throws Exception {
        Film film1 = Film.builder()
                .name("Titanic")
                .description("Titanic description")
                .releaseDate(LocalDate.of(2012, 6, 30))
                .duration(60)
                .build();
        Film film2 = Film.builder()
                .name("Avatar")
                .description("Avatar description")
                .releaseDate(LocalDate.of(2013, 12, 10))
                .duration(160)
                .build();
        Film film3 = Film.builder()
                .name("Spider Man")
                .description("Spider Man description")
                .releaseDate(LocalDate.of(2010, 11, 15))
                .duration(100)
                .build();
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film3))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/films")
                                .content(objectMapper.writeValueAsString(List.of(film1, film2, film3)))
                                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }
}