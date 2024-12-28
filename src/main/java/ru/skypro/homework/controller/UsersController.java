package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UsersController {
    Logger logger = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    private UserService userService;

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Используем username как идентификатор пользователя

        try {
            // Вызов сервиса для обновления пароля
            userService.updatePassword(currentUserEmail, newPassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NewPasswordException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Используем username как идентификатор пользователя

        // Вызов сервиса для получения данных пользователя
        User user = userService.findUserByUsername(currentUsername);

        // Проверка, что пользователь найден
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Метод для обновления информации об авторизованном пользователе
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для обновления данных пользователя
        UpdateUser user = userService.updateUser(currentUsername, updateUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping(value= "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestParam("image") MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Вошли в метод uploadUserImage, класса UserController. Принят файл image: " + image.toString());
        if (authentication != null) {
            logger.info("Пользователь обнаружен");
            userService.updateUserImage(authentication.getName(), image);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
