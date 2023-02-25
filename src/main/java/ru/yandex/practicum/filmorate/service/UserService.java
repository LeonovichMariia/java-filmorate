package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService<User> {

    @Autowired
    public UserService(Storage<User> storage) {
        this.storage = storage;
    }

    @Override
    public void validate(User user) throws ValidationException {
        if (user != null) {
            if (user.getName() == null || user.getName().isBlank()) {
                log.warn(LogMessages.EMPTY_USER_NAME.toString());
                user.setName(user.getLogin());
            }
        } else {
            log.warn(LogMessages.NULL_USER.toString(), user);
        }
    }

    public void addFriend(Long userId, Long friendId) {
        User user = storage.findObjectById(userId);
        User friend = storage.findObjectById(friendId);
        if (user == null && friend == null) {
            log.warn(LogMessages.NULL_USER.toString());
            return;
        }
        assert user != null;
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info(LogMessages.FRIEND_ADDED.toString(), friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = storage.findObjectById(userId);
        User friend = storage.findObjectById(friendId);
        if (user == null && friend == null) {
            log.warn(LogMessages.NULL_USER.toString());
            return;
        }
        assert user != null;
        user.removeFriend(friendId);
        friend.removeFriend(userId);
        log.info(LogMessages.FRIEND_REMOVED.toString(), friend);
    }

    public List<User> getFriends(Long userId) {
        User user = storage.findObjectById(userId);
        if (user == null ) {
            log.warn(LogMessages.NULL_USER.toString());
            return null;
        }
        log.info(LogMessages.LIST_OF_FRIENDS.toString(), userId);
        return user.getFriends().stream()
                .map(storage::findObjectById)
                .collect(Collectors.toList());

    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = storage.findObjectById(id);
        User otherUser = storage.findObjectById(otherId);
        if (user == null && otherUser == null) {
            log.warn(LogMessages.NULL_USER.toString());
            return null;
        }
        log.info(LogMessages.LIST_OF_COMMON_FRIENDS.toString());
        assert user != null;
        List<Long> userFriends = user.getFriends();
        userFriends.retainAll(otherUser.getFriends());
        return userFriends.stream()
                .map(storage::findObjectById)
                .collect(Collectors.toList());
    }
}