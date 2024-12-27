package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.mapper.UserMapperImpl;
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
 * Этот класс предоставляет методы для управления данными пользователей, включая проверку пароля,
 * обновление пароля, поиск пользователей по email и id, обновление информации о пользователе
 * и обновление изображения пользователя.
 *
 * @Service Аннотация, указывающая, что этот класс является сервисом в Spring.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserMapperImpl userMapper;

    /**
     * Проверяет, совпадает ли текущий пароль пользователя с предоставленным паролем.
     *
     * @param email            Email пользователя, для которого проверяется пароль.
     * @param currentPassword  Текущий пароль для проверки.
     * @return true, если пароль совпадает, иначе false.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public boolean checkCurrentPassword(String email, String currentPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().hashCode() == currentPassword.hashCode(); // Предполагается, что пароль уже захеширован
        } else {
            throw new UserNotFoundException("User not found with id: " + email);
        }
    }

    /**
     * Обновляет пароль пользователя после проверки текущего пароля.
     *
     * @param email         Email пользователя, для которого обновляется пароль.
     * @param newPassword   Новый пароль и текущий пароль для проверки.
     * @throws NewPasswordException если текущий пароль неверен.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public void updatePassword(String email, NewPassword newPassword) throws NewPasswordException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Проверка текущего пароля
            if (!checkCurrentPassword(email, newPassword.getCurrentPassword())) {
                throw new NewPasswordException("Current password is incorrect");
            }

            // Установка нового пароля
            user.setPassword(newPassword.getNewPassword()); // Предполагается, что пароль уже захеширован
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + email);
        }
    }

    /**
     * Находит пользователя по его email.
     *
     * @param email Email пользователя для поиска.
     * @return Optional, содержащий пользователя, если он найден, иначе пустой Optional.
     */
    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Находит пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя для поиска.
     * @return Optional, содержащий пользователя, если он найден, иначе пустой Optional.
     */
    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param email      Email пользователя, информацию которого нужно обновить.
     * @param updateUser DTO с обновленными данными пользователя.
     * @return Обновленный пользователь.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public User updateUser(String email, UpdateUser updateUser) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Преобразуем DTO в сущность с помощью маппера
            User updatedUser = userMapper.mapFromUpdateUser(updateUser);

            // Обновляем только измененные поля
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setPhone(updatedUser.getPhone());

            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }

    /**
     * Обновляет изображение пользователя.
     *
     * @param email Email пользователя, изображение которого нужно обновить.
     * @param image Новое изображение пользователя.
     * @throws RuntimeException если произошла ошибка при загрузке изображения.
     * @throws UserNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public void updateUserImage(String email, MultipartFile image) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                // Используем ImageService для сохранения изображения
                imageService.uploadUserImage(email, image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update user image", e);
            }
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }
}
