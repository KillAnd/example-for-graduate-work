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

/**
 * REST-контроллер для управления данными пользователей.
 * Предоставляет эндпоинты для выполнения операций с пользователями, таких как:
 * <ul>
 *     <li>Изменение пароля пользователя</li>
 *     <li>Получение данных текущего пользователя</li>
 *     <li>Обновление информации о пользователе</li>
 *     <li>Обновление изображения профиля пользователя</li>
 * </ul>
 * Все операции доступны только авторизованным пользователям.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    /**
     * Изменяет пароль текущего пользователя.
     *
     * @param newPassword Объект {@link NewPassword}, содержащий новый пароль.
     * @return {@link ResponseEntity} с пустым телом и статусом {@link HttpStatus#OK}, если пароль успешно изменен.
     *         Если новый пароль не соответствует требованиям, возвращает статус {@link HttpStatus#BAD_REQUEST}.
     */
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        try {
            userService.updatePassword(currentUserEmail, newPassword);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NewPasswordException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Получает данные текущего авторизованного пользователя.
     *
     * @return {@link ResponseEntity} с объектом {@link Optional<User>}, содержащим данные пользователя.
     *         Если пользователь не найден, выбрасывает исключение {@link UnauthorizedException}.
     */
    @GetMapping("/me")
    public ResponseEntity<Optional<User>> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Optional<User> user = userService.findUserByEmail(currentUserEmail);

        if (user.isEmpty()) {
            throw new UnauthorizedException("User not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Обновляет информацию о текущем авторизованном пользователе.
     *
     * @param updateUser Объект {@link UpdateUser}, содержащий новые данные пользователя.
     * @return {@link ResponseEntity} с обновленным объектом {@link User}.
     */
    @PatchMapping("/me")
    public ResponseEntity<User> updateUser(@RequestBody UpdateUser updateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User updatedUser = userService.updateUser(currentUserEmail, updateUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Обновляет изображение профиля текущего авторизованного пользователя.
     *
     * @param image Новое изображение профиля.
     * @return {@link ResponseEntity} с сообщением об успешном обновлении изображения.
     *         Если пользователь не найден, возвращает статус {@link HttpStatus#NOT_FOUND}.
     *         Если произошла ошибка при обработке изображения, возвращает статус {@link HttpStatus#INTERNAL_SERVER_ERROR}.
     */
    @PatchMapping("/me/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();

            userService.updateUserImage(currentUserEmail, image);
            return ResponseEntity.ok("Image updated successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update image");
        }
    }
}
