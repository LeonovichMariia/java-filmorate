package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Friend;
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
    public void validate(User user) {
        checkIfObjectNull(user);
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn(LogMessages.EMPTY_USER_NAME.toString());
            user.setName(user.getLogin());
        }
    }

    public void addFriend(Long userId, Long otherUserId) {
        User user = storage.findObjectById(userId);
        User otherUser = storage.findObjectById(otherUserId);
        checkIfObjectNull(user);
        checkIfObjectNull(otherUser);
        User friend = getFriend(userId, otherUserId);
        if (friend != null) {
            log.info(LogMessages.FRIEND_UNCONFIRMED.toString());
            Friend unconfirmedFriend = user.getFriends().stream()
                    .filter(f -> f.getUserId() == otherUserId)
                    .findFirst()
                    .orElse(null);
            Friend otherUnconfirmedFriend = user.getFriends().stream()
                    .filter(f -> f.getUserId() == otherUserId)
                    .findFirst()
                    .orElse(null);
            if(unconfirmedFriend == null || otherUnconfirmedFriend == null) {
                log.warn(LogMessages.NULL_OBJECT.toString());
                throw new ObjectNotFoundException("Неизвестный объект!");
            } else {
                unconfirmedFriend.setFriendshipConfirmed(true);
                otherUnconfirmedFriend.setFriendshipConfirmed(true);
                log.info(LogMessages.FRIEND_CONFIRMED.toString());
            }
        } else {
            user.addFriend(otherUserId);
            otherUser.addFriend(userId);
            log.info(LogMessages.FRIEND_ADDED.toString(), otherUser);
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = storage.findObjectById(userId);
        User friend = storage.findObjectById(friendId);
        checkIfObjectNull(user);
        checkIfObjectNull(friend);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
        log.info(LogMessages.FRIEND_REMOVED.toString(), friend);
    }

    public List<User> getFriends(Long userId) {
        User user = storage.findObjectById(userId);
        checkIfObjectNull(user);
        log.info(LogMessages.LIST_OF_FRIENDS.toString(), userId);
        return user.getFriends().stream()
                .map(Friend::getUserId)
                .map(storage::findObjectById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = storage.findObjectById(id);
        User otherUser = storage.findObjectById(otherId);
        checkIfObjectNull(user);
        checkIfObjectNull(otherUser);
        log.info(LogMessages.LIST_OF_COMMON_FRIENDS.toString());
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(Friend::getUserId)
                .map(storage::findObjectById)
                .collect(Collectors.toList());
    }

    public User getFriend(Long id, Long friendId) {
        return getFriends(id).stream().filter(user -> user.getId() == friendId).findFirst().orElse(null);
    }
}