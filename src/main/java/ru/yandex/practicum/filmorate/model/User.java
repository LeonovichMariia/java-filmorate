package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.messages.AnnotationMessages;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractObject {
    @Builder
    public User(Long id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    @Email(message = AnnotationMessages.INCORRECT_EMAIL)
    @NotBlank(message = AnnotationMessages.EMPTY_EMAIL)
    private String email;
    @NotBlank(message = AnnotationMessages.EMPTY_LOGIN)
    @Pattern(regexp = "\\S+", message = AnnotationMessages.INCORRECT_LOGIN)
    private String login;
    private String name;
    @PastOrPresent(message = AnnotationMessages.INCORRECT_BIRTH_DATE)
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friends = new HashSet<>();

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}