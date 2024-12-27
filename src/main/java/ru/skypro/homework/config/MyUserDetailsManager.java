package ru.skypro.homework.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис для управления данными пользователей в Spring Security.
 * Реализует интерфейс {@link UserDetailsService} для загрузки данных пользователя по email.
 * Также предоставляет методы для создания новых пользователей и проверки их существования.
 */
@Service
public class MyUserDetailsManager implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для создания экземпляра MyUserDetailsManager.
     *
     * @param userRepository  Репозиторий для работы с сущностью {@link User}.
     * @param passwordEncoder Кодировщик паролей для шифрования паролей пользователей.
     */
    public MyUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Загружает данные пользователя по его email.
     *
     * @param phone Email пользователя.
     * @return Объект {@link UserDetails}, содержащий данные пользователя.
     * @throws UsernameNotFoundException Если пользователь с указанным email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(phone);
        if (user.isEmpty()) {
            throw new UserNotFoundException(phone);
        }
        return new MyUserDetails(user.get());
    }

    /**
     * Создает нового пользователя на основе данных из {@link UserDetails}.
     *
     * @param userDetails Объект {@link UserDetails}, содержащий данные нового пользователя.
     */
    public void createUser(UserDetails userDetails) {
        User user = new User();
        user.setEmail(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        user.setRole(Role.valueOf(userDetails.getAuthorities().iterator().next().getAuthority()));
        userRepository.save(user);
    }

    /**
     * Проверяет, существует ли пользователь с указанным email.
     *
     * @param username Email пользователя.
     * @return {@code true}, если пользователь существует, иначе {@code false}.
     */
    public boolean userExists(String username) {
        return userRepository.findByEmail(username).isPresent();
    }
}
