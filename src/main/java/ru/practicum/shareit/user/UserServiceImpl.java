package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.userDtoToUser(userDto);
        if (repository.isEmailExisted(user.getEmail())) {
            throw new ValidateException("Пользователь с электронной почтой " + userDto.getEmail() + " уже существует");
        }
        log.info("Пользователь с электронной почтой " + userDto.getEmail() + "  был успешно создан");
        return UserMapper.userToUserDto(repository.createUser(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : repository.getAllUsers()) {
            usersDto.add(UserMapper.userToUserDto(user));
        }
        log.info("Получены все пользователи");
        return usersDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.getUserById(id);
        log.info("Получен пользователь с id {}", id);
        return UserMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = repository.getUserById(id);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            if (repository.isEmailExisted(userDto.getEmail())) {
                if (!userDto.getEmail().equals(user.getEmail())) {
                    log.info("Пользователь с id = {} уже существует", user.getId());
                    throw new ValidateException("Пользователь с электронной почтой " + userDto.getEmail()
                            + " уже существует");
                }
            }
            user.setEmail(userDto.getEmail());
        }
        log.info("Получен пользователь с id = {}", user.getId());
        return UserMapper.userToUserDto(user);
    }

    @Override
    public UserDto removeUserById(Long id) {
        User user = repository.removeUserById(id);
        log.info("Пользователь с id = {} удален", id);
        return UserMapper.userToUserDto(user);
    }
}
