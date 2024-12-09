package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.UserService;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

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
    public ResponseEntity<User> getUser() {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для получения данных пользователя
        User user = userService.findUserById(currentUserEmail);

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
        String currentUserEmail = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для обновления данных пользователя
        User updatedUser = userService.updateUser(currentUserEmail, updateUser);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/me/image")
    public ResponseEntity<byte[]> updateUserImage(@RequestParam("image") MultipartFile image) {
        // Получение текущего пользователя из контекста безопасности
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); // Используем email как идентификатор пользователя

        // Вызов сервиса для получения данных пользователя
        User user = userService.findUserById(currentUserEmail);

        // Проверка, что пользователь найден и у него есть изображение
        if (user == null || user.getImage() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Возврат изображения в виде бинарных данных
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(user.getImage(), headers, HttpStatus.OK);
    }
}
