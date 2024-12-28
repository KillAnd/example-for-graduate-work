package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.exception.UpdateUserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Data
public class UpdateUser {

    @Schema(minLength = 3, maxLength = 10, description = "Имя пользователя")
    private String firstName;

    @Schema(minLength = 3, maxLength = 10, description = "Фамилия пользователя")
    private String lastName;

    @Schema(pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}\n",
            description = "Телефон пользователя")
    private String phone;
}
