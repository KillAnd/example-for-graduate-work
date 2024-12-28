package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.exception.NewPasswordException;
@Data
public class NewPassword {

    @Schema(minLength = 8, maxLength = 16, description = "Текущий пароль")
    private String currentPassword;

    @Schema(minLength = 8, maxLength = 16, description = "Новый пароль")
    private String newPassword;
}
