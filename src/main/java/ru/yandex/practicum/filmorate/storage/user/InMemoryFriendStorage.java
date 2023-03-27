package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "memory")
public class InMemoryFriendStorage implements  FriendStorage {
    private UserStorage storage;

    @Autowired
    public InMemoryFriendStorage(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public void addFriend(Long userId, Long otherUserId) {
        User user = storage.findObjectById(userId);
        User otherUser = storage.findObjectById(otherUserId);
        user.addFriend(otherUserId);
        otherUser.addFriend(userId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = storage.findObjectById(userId);
        User friend = storage.findObjectById(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = storage.findObjectById(userId);
        return user.getFriends().stream()
                .map(User::getId)
                .map(storage::findObjectById)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = storage.findObjectById(id);
        User otherUser = storage.findObjectById(otherId);
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(User::getId)
                .map(storage::findObjectById)
                .collect(Collectors.toList());
    }
}
