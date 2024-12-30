package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.impl.AuthServiceImpl;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        logger.info("Вход в метод login, класса AuthController. Принят объект login: {}", login.toString());
        if (authService.login(login.getUsername(), login.getPassword())) {
            logger.info("Успешная авторизация");
            return ResponseEntity.ok().build();
        } else {
            logger.info("Ошибка авторизации, такого пользователя нет");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        logger.info("Вход в метод register, класса AuthController. Принят объект register: {}", register.toString());
        if (authService.register(register)) {
            logger.info("юзер создан");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            logger.info("плохой реквест");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
