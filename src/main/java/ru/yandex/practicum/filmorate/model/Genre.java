package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Genre extends AbstractObject {
    private String name;

    @Builder
    public Genre(long id, String name) {
        super(id);
        this.name = name;
    }
}