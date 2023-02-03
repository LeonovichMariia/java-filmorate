package ru.yandex.practicum.filmorate.messages;

public enum LogMessages {
    TOTAL("Текущее количество объектов: {}"),
    ALREADY_EXIST("Такой объект {} уже есть"),
    OBJECT_ADD("Объект {} сохранен"),
    OBJECT_UPDATE("Объект {} обновлен"),
    OBJECT_NOT_FOUND("Объект {} не найден");

    private final String messageText;

    LogMessages(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return messageText;
    }
}