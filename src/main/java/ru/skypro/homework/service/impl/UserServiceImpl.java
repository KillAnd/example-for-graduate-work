package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.mapper.UserMapperImpl;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Optional;

/**
 * Реализация сервиса для работы с пользователями.
 * Этот класс предоставляет методы для управления пользователями, включая обновление пароля,
 * данных пользователя и изображения профиля.
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserMapperImpl userMapper;

    /**
     * Проверяет, совпадает ли текущий пароль пользователя с предоставленным паролем.
     *
     * @param username        имя пользователя
     * @param currentPassword текущий пароль для проверки
     * @return true, если пароль совпадает, иначе false
     * @throws UserNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public boolean checkCurrentPassword(String username, String currentPassword) {
        User byUsername = userRepository.findByUsername(username);
        if (byUsername != null) {
            return encoder.matches(currentPassword, byUsername.getPassword());
        } else {
            logger.info("Юзер отсутствует");
            throw new UserNotFoundException("User not found with id: " + username);
        }
    }

    /**
     * Обновляет пароль пользователя.
     *
     * @param username    имя пользователя
     * @param newPassword объект, содержащий текущий и новый пароль
     * @throws NewPasswordException  если текущий пароль неверен
     * @throws UserNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public void updatePassword(String username, NewPassword newPassword) throws NewPasswordException {
        User userUpdated = userRepository.findByUsername(username);
        if (userUpdated != null) {
            if (!checkCurrentPassword(username, newPassword.getCurrentPassword())) {
                throw new NewPasswordException("Current password is incorrect");
            }
            userUpdated.setPassword(encoder.encode(newPassword.getNewPassword()));
            userRepository.save(userUpdated);
        } else {
            throw new UserNotFoundException("User not found with id: " + username);
        }
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return Optional, содержащий пользователя, если он найден, иначе пустой Optional
     */
    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Находит пользователя по его имени.
     *
     * @param username имя пользователя
     * @return найденный пользователь
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param username имя пользователя
     * @param update   объект, содержащий новые данные пользователя
     * @return обновленные данные пользователя
     * @throws UserNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UpdateUser updateUser(String username, UpdateUser update) {
        User userOptional = userRepository.findByUsername(username);
        if (userOptional != null) {
            userOptional.setFirstName(update.getFirstName());
            userOptional.setLastName(update.getLastName());
            userOptional.setPhone(update.getPhone());
            userRepository.save(userOptional);
            return update;
        } else {
            throw new UserNotFoundException("User not found with email: " + username);
        }
    }

    /**
     * Обновляет изображение профиля пользователя.
     *
     * @param username имя пользователя
     * @param image    файл изображения
     * @throws RuntimeException      если произошла ошибка при загрузке изображения
     * @throws UserNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public void updateUserImage(String username, MultipartFile image) {
        User userFind = userRepository.findByUsername(username);
        if (userFind != null) {
            try {
                Image imageAdded = imageService.uploadImage(image);
                logger.info("Отправка в image service прошла успешно");
                userFind.setImage(imageAdded.getFilePath());
                logger.info("Изображение сохранилось в базу данных");
                userFind.setImageUsers(imageAdded);
                userRepository.save(userFind);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update user image", e);
            }
        } else {
            throw new UserNotFoundException("User not found with email: " + username);
        }
    }
}