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
import ru.skypro.homework.dto.UserDTO;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.User;
import ru.skypro.homework.exception.NewPasswordException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;

import java.util.Optional;

/**
 * Контроллер для обработки запросов, связанных с пользователями.
 * Предоставляет методы для обновления пароля, получения данных пользователя,
 * обновления информации и изображения профиля.
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private UserService userService;

    /**
     * Обновляет пароль текущего пользователя.
     *
     * @param newPassword объект, содержащий текущий и новый пароль
     * @return ResponseEntity с HTTP-статусом OK, если пароль успешно обновлен,
     *         или BAD_REQUEST, если текущий пароль неверен
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
     * Возвращает данные текущего пользователя.
     *
     * @return ResponseEntity с данными пользователя и HTTP-статусом OK,
     *         или UNAUTHORIZED, если пользователь не найден
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UserDTO user = userService.findUserByUsername(currentUsername);

        if (user == null) {
            throw new UnauthorizedException("User not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param updateUser объект, содержащий новые данные пользователя
     * @return ResponseEntity с обновленными данными пользователя и HTTP-статусом OK
     */
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        UpdateUser user = userService.updateUser(currentUsername, updateUser);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Обновляет изображение профиля текущего пользователя.
     *
     * @param image файл изображения
     * @return ResponseEntity с HTTP-статусом OK, если изображение успешно обновлено,
     *         или UNAUTHORIZED, если пользователь не авторизован
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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