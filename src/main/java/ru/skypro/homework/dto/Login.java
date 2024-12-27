package ru.skypro.homework.dto;

import lombok.Data;

/**
 * Класс, представляющий данные для аутентификации пользователя.
 * Содержит email и пароль пользователя.
 * Используется для передачи данных между слоями приложения.
 */
@Data
public class Login {

    /**
     * Email пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;
}