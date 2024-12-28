package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetailsManager;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

/**
 * Реализация сервиса для аутентификации и регистрации пользователей.
 * Этот класс предоставляет методы для входа в систему и регистрации новых пользователей.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final MyUserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapperImpl userMapper;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param manager        менеджер для работы с пользователями
     * @param encoder        кодировщик паролей
     * @param userRepository репозиторий для работы с пользователями
     * @param userMapper     маппер для преобразования DTO в сущности пользователей
     */
    public AuthServiceImpl(MyUserDetailsManager manager, PasswordEncoder encoder, UserRepository userRepository, UserMapperImpl userMapper) {
        this.manager = manager;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Выполняет вход пользователя в систему.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return true, если вход выполнен успешно, иначе false
     */
    @Override
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            logger.info("Пользователя не существует");
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(username);
        logger.info("Информация о загруженном пользователе: {} | {}", userDetails.getUsername(), userDetails.getPassword());
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register объект, содержащий данные для регистрации
     * @return true, если регистрация выполнена успешно, иначе false
     */
    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        register.setPassword(encoder.encode(register.getPassword()));
        userRepository.save(userMapper.toUserEntity(register));
        return true;
    }
}