package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractObject {
    private Long id;
}