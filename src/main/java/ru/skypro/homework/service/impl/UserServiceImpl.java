package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    Logger logger;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    @Autowired
    private UserMapperImpl userMapper;

    @Override
    public boolean checkCurrentPassword(String username, String currentPassword) {
        //Метод для проверки пароля пользователя
        User byUsername = userRepository.findByUsername(username);
        if (byUsername != null ) {
            return true;
        } else {
            logger.info("Юзер отсутствует");
            throw new UserNotFoundException("User not found with id: " + username);
        }
    }

    @Override// Метод для обновления пароля пользователя
    public void updatePassword(String username, NewPassword newPassword) throws NewPasswordException {
        User userUpdated = userRepository.findByUsername(username);
        if (userUpdated != null) {
            // Проверка текущего пароля
            if (!checkCurrentPassword(username, newPassword.getCurrentPassword())) {
                throw new NewPasswordException("Current password is incorrect");
            }
            // Установка нового пароля
            userUpdated.setPassword(newPassword.getNewPassword()); // Предполагается, что пароль уже захеширован
            userRepository.save(userUpdated);
        } else {
            throw new UserNotFoundException("User not found with id: " + username);
        }
    }

    @Override
    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateUser(String username, UpdateUser updateUser) {
        User userOptional = userRepository.findByUsername(username);
        if (userOptional != null) {

            // Преобразуем DTO в сущность с помощью маппера
            User updatedUser = userMapper.mapFromUpdateUser(updateUser);

            // Обновляем только измененные поля
            updateUser.setFirstName(updatedUser.getFirstName());
            updatedUser.setLastName(updatedUser.getLastName());
            updatedUser.setPhone(updatedUser.getPhone());

            return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException("User not found with email: " + username);
        }
    }

    @Override
    public void updateUserImage(String username, MultipartFile image) {
        User userFind = userRepository.findByUsername(username);
        if (userFind != null) {
            try {
                // Используем ImageService для сохранения изображения
                Image imageAdded = imageService.uploadImage(image);
                logger.info("Отправка в image service прошла успешно");
                userRepository.saveImagePath(imageAdded.getFilePath(), username);
                logger.info("Изображение сохранилось в базу данных");

            } catch (IOException e) {
                throw new RuntimeException("Failed to update user image", e);
            }
        } else {
            throw new UserNotFoundException("User not found with email: " + username);
        }
    }
}
