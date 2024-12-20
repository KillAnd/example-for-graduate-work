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

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    @Autowired
    private UserMapperImpl userMapper;

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
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

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

    @Override
    public void updateUserImage(String email, MultipartFile image) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                // Используем ImageService для сохранения изображения
                imageService.uploadImage(email, image);
            } catch (IOException e) {
                throw new RuntimeException("Failed to update user image", e);
            }
        } else {
            throw new UserNotFoundException("User not found with email: " + email);
        }
    }
}
