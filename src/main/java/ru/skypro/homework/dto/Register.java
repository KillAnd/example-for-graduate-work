package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Register {

    @Schema(minLength = 4, maxLength = 32, description = "Логин")
    private String username;

    @Schema(minLength = 8, maxLength = 16, description = "Пароль")
    private String password;

    @Schema(minLength = 2, maxLength = 16, description = "Имя пользователя")
    private String firstName;

    @Schema(minLength = 2, maxLength = 16, description = "Фамилия пользователя")
    private String lastName;

    @Schema(pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}\n", description = "Телефон пользователя")
    private String phone;

    @Schema(examples = {"USER", "ADMIN"}, description = "Роль пользователя")
    private Role role;
}
