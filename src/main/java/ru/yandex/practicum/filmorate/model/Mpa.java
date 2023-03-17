package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Mpa extends AbstractObject{
    private String name;

    @Builder
    public Mpa(long id, String name) {
        super(id);
        this.name = name;
    }
}