package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean checkCurrentPassword(String email, String currentPassword) {
        //Метод для проверки пароля пользователя
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().hashCode() == currentPassword.hashCode(); // Предполагается, что пароль уже захеширован
        } else {
            throw new UserNotFoundException("User not found with id: " + email);
        }
    }

    @Override// Метод для обновления пароля пользователя
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

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail();
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findById(Long.valueOf(id)).get();
    }

    @Override
    public User updateUser(String email, UpdateUser updateUser) {
        // Метод для обновления данных пользователя
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Обновляем поля пользователя на основе данных из updateUser
            user.setFirstName(updateUser.getFirstName());
            user.setLastName(updateUser.getLastName());
            user.setPhone(updateUser.getPhone());
            return userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + email);
        }
    }

    @Override
    public void updateUserImage(String email, MultipartFile image) {
        //метод для изменения аватарки
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                // Преобразование изображения в массив байтов
                byte[] imageBytes = image.getBytes();
                user.setImage(imageBytes);
                userRepository.save(user);
            } catch (IOException e) {
                // Обработка ошибки чтения изображения
                throw new RuntimeException("Failed to read image", e);
            }
        } else {
            throw new UserNotFoundException("User not found with id: " + email);
        }
    }
}
