package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Используем email как идентификатор пользователя

        try {
            // Вызов сервиса для обновления пароля
            userService.updatePassword(currentUserEmail, newPassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NewPasswordException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/me")
    public ResponseEntity<Optional<User>> getUser() {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для получения данных пользователя
        Optional<User> user = userService.findUserByEmail(currentUserEmail);

        // Проверка, что пользователь найден
        if (user.isEmpty()) {
            throw new UnauthorizedException("User not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Метод для обновления информации об авторизованном пользователе
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для обновления данных пользователя
        User updatedUser = userService.updateUser(currentUserEmail, updateUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/me/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image) {
        try {
            // Получение текущего пользователя из контекста безопасности
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            // Вызов сервиса для обновления изображения пользователя
            userService.updateUserImage(currentUserEmail, image);

            // Возвращаем успешный ответ
            return ResponseEntity.ok("Image updated successfully");
        } catch (UserNotFoundException e) {
            // Обработка случая, если пользователь не найден
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Обработка других ошибок
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update image");
        }
    }
}
