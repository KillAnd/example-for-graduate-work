package ru.skypro.homework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

/**
 * Реализация интерфейса UserDetailsManager для управления пользователями.
 * Этот класс предоставляет методы для загрузки пользователей по имени и проверки их существования.
 */
@Service
public class MyUserDetailsManager implements UserDetailsManager {

    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsManager.class);

    private final UserRepository userRepository;

    /**
     * Конструктор для инициализации менеджера пользователей.
     *
     * @param userRepository репозиторий для работы с пользователями
     */
    public MyUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по его имени.
     *
     * @param username имя пользователя
     * @return объект UserDetails, представляющий пользователя
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException(username);
        }
        return new MyUserDetails(user);
    }

    /**
     * Создает нового пользователя. (Не реализовано)
     *
     * @param user объект UserDetails, представляющий нового пользователя
     */
    @Override
    public void createUser(UserDetails user) {
        // Метод не реализован
    }

    /**
     * Обновляет данные пользователя. (Не реализовано)
     *
     * @param user объект UserDetails, представляющий обновленного пользователя
     */
    @Override
    public void updateUser(UserDetails user) {
        // Метод не реализован
    }

    /**
     * Удаляет пользователя. (Не реализовано)
     *
     * @param username имя пользователя
     */
    @Override
    public void deleteUser(String username) {
        // Метод не реализован
    }

    /**
     * Изменяет пароль пользователя. (Не реализовано)
     *
     * @param oldPassword старый пароль
     * @param newPassword новый пароль
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // Метод не реализован
    }

    /**
     * Проверяет, существует ли пользователь с указанным именем.
     *
     * @param username имя пользователя
     * @return true, если пользователь существует, иначе false
     */
    @Override
    public boolean userExists(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.info("NULL");
            return false;
        } else {
            logger.info("NO NULL");
            return true;
        }
    }
}