package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь с электронной почтой = {} был добавлен в репозиторий", user.getEmail());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получены все пользователи из репозитория");
        return List.copyOf(users.values());
    }

    @Override
    public User getUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id =" + id + "не существует");
        }
        log.info("Пользователь с id = {} получен из репозитория", id);
        return users.get(id);
    }

    @Override
    public User removeUserById(Long id) {
        User memoryUser = getUserById(id);
        users.remove(id);
        log.info("Пользователь с id = {} удален", id);
        return memoryUser;
    }

    public final Boolean isEmailExisted(String email) {
        for (User userList : users.values()) {
            if (userList.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }
}
