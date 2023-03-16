package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.messages.LogMessages;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.storage.user.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends AbstractService<User> {
    private FriendStorage friendStorage;

    @Autowired
    public UserService(Storage<User> storage, FriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
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
            friendStorage.addFriend(userId, otherUserId);
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
        friendStorage.removeFriend(userId, friendId);
        log.info(LogMessages.FRIEND_REMOVED.toString(), friend);
    }

    public List<User> getFriends(Long userId) {
        User user = storage.findObjectById(userId);
        checkIfObjectNull(user);
        log.info(LogMessages.LIST_OF_FRIENDS.toString(), userId);
        return friendStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = storage.findObjectById(id);
        User otherUser = storage.findObjectById(otherId);
        checkIfObjectNull(user);
        checkIfObjectNull(otherUser);
        log.info(LogMessages.LIST_OF_COMMON_FRIENDS.toString());
        return friendStorage.getCommonFriends(id, otherId);
    }

    public User getFriend(Long id, Long friendId) {
        return getFriends(id).stream().filter(user -> user.getId() == friendId).findFirst().orElse(null);
    }
}