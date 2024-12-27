package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.config.MyUserDetailsManager;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * Реализует интерфейс {@link AuthService} и предоставляет методы для входа в систему
 * и регистрации новых пользователей.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final MyUserDetailsManager manager;
    private final PasswordEncoder encoder;

    /**
     * Конструктор для создания экземпляра AuthServiceImpl.
     *
     * @param manager        Менеджер для работы с данными пользователей.
     * @param passwordEncoder Кодировщик паролей для шифрования паролей пользователей.
     */
    public AuthServiceImpl(MyUserDetailsManager manager,
                           PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.encoder = passwordEncoder;
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param email    Email пользователя.
     * @param password Пароль пользователя.
     * @return {@code true}, если аутентификация прошла успешно, иначе {@code false}.
     */
    @Override
    public boolean login(String email, String password) {
        if (!manager.userExists(email)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(email);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param register Объект {@link Register}, содержащий данные для регистрации.
     * @return {@code true}, если регистрация прошла успешно, иначе {@code false}.
     */
    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getEmail())) {
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getEmail())
                        .roles(register.getRole().name())
                        .build());
        return true;
    }
}
