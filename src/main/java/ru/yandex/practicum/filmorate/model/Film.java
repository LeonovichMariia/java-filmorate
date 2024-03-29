package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.messages.AnnotationMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class Film extends AbstractObject {
    @Builder
    public Film(long id, String name, String description, LocalDate releaseDate, int duration, long rate, Mpa mpa) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
    }

    @NotBlank(message = AnnotationMessages.EMPTY_NAME)
    private String name;
    @Size(max = 200, message = AnnotationMessages.LONG_DESCRIPTION)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive(message = AnnotationMessages.INCORRECT_DURATION)
    private int duration;
    @JsonIgnore
    private final Set<Long> filmAudience = new HashSet<>();
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    private Long rate;

    public void addLike(Long id) {
        filmAudience.add(id);
        rate++;
    }

    public void removeLike(Long id) {
        filmAudience.remove(id);
        rate--;
    }
}