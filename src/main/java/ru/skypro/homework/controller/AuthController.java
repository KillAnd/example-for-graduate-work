package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * REST-контроллер для обработки запросов, связанных с аутентификацией и регистрацией пользователей.
 * Предоставляет эндпоинты для входа в систему и регистрации новых пользователей.
 * Поддерживает кросс-доменные запросы с указанного источника (http://localhost:3000).
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Обрабатывает запрос на вход в систему.
     *
     * @param login Объект {@link Login}, содержащий email и пароль пользователя.
     * @return {@link ResponseEntity} с пустым телом и статусом {@link HttpStatus#OK}, если аутентификация прошла успешно.
     *         Если аутентификация не удалась, возвращает статус {@link HttpStatus#UNAUTHORIZED}.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (authService.login(login.getEmail(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     *
     * @param register Объект {@link Register}, содержащий данные для регистрации пользователя.
     * @return {@link ResponseEntity} с пустым телом и статусом {@link HttpStatus#CREATED}, если регистрация прошла успешно.
     *         Если регистрация не удалась, возвращает статус {@link HttpStatus#BAD_REQUEST}.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
