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
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.UnauthorizedException;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для обновления пароля
        userService.updatePassword(currentUserId, newPassword.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для получения данных пользователя
        User user = userService.findUserById(currentUserId);

        // Проверка, что пользователь найден
        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Метод для обновления информации об авторизованном пользователе
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для обновления данных пользователя
        User updatedUser = userService.updateUser(currentUserId, updateUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile image) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для обновления аватара пользователя
        userService.updateUserImage(currentUserId, image);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
