package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.exception.CreateOrUpdateAdException;
@Data
public class CreateOrUpdateAd {

    @Schema(minLength = 4, maxLength = 32, description = "Заголовок объявления")
    private String title;

    @Schema(minimum = "0", maximum = "10000000", description = "Цена объявления")
    private Integer price;

    @Schema(minLength = 8, maxLength = 64, description = "Описание объявления")
    private String description;

}
