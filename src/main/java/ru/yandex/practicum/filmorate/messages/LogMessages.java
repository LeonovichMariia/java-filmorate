package ru.yandex.practicum.filmorate.messages;

public enum LogMessages {
    TOTAL("Текущее количество объектов: {}"),
    ALREADY_EXIST("Такой объект {} уже есть"),
    OBJECT_ADD("Объект {} сохранен"),
    OBJECT_UPDATE("Объект {} обновлен"),
    OBJECT_NOT_FOUND("Объект {} не найден"),
    INCORRECT_FILM_RELEASE_DATE("Некорректная дата релиза! Дата релиза не может быть раньше 28.12.1895"),
    EMPTY_USER_NAME("Имя пользователя пустое. Использован логин"),
    NULL_USER("Неизвестный пользователь"),
    NULL_FILM("Неизвестный фильм"),
    OBJECT_DELETED("Объект {} удален"),
    GET_ALL_FILMS("Запрос на получение списка всех фильмов"),
    GET_ALL_USERS("Запрос на получение списка всех пользователей"),
    GET_FILM_BY_ID("Запрос на получение фильма с id {} "),
    DELETE_FILM_BY_ID("Запрос на удаление фильма с id {} "),
    DELETE_USER_BY_ID("Запрос на удаление пользователя с id {} "),
    GET_FRIEND_BY_ID("Запрос на получение друга с id {} "),
    ADD_LIKE("Запрос на добавление лайка фильму {} пользователем {} "),
    LIKED_FILM("Фильму {} поставлен лайк"),
    UNLIKED_FILM("У фильма {} удален лайк"),
    POPULAR_TOTAL("Количество популярных фильмов: {}"),
    REMOVE_LIKE("Запрос на удаление лайка у фильма {} пользователем {} "),
    GET_POPULAR("Запрос на получение списка самых популярных фильмов"),
    ADD_FRIEND("Запрос на добавление в друзья {} пользователем {} "),
    FRIEND_ADDED("{} добавлен в друзья"),
    REMOVE_FRIEND("Запрос на удаление из друзей {} пользователем {} "),
    FRIEND_REMOVED("{} удален из друзей"),
    GET_FRIENDS("Запрос на получение списка всех друзей"),
    LIST_OF_FRIENDS("Список всех друзей: "),
    GET_COMMON_FRIENDS("Запрос на получение списка общих друзей у пользователей {} {} "),
    LIST_OF_COMMON_FRIENDS("Список общих друзей: "),;

    private final String messageText;

    LogMessages(String messageText) {
        this.messageText = messageText;
    }
}