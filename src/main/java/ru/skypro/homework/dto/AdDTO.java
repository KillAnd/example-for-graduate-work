package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Id;
@Data
public class AdDTO {
    @Schema(description = "id автора объявления")
    private Integer author;

    @Schema(description = "Ссылка на картинку объявления")
    private String image;

    @Schema(description = "id объявления")
    @Id
    private Integer pk;

    @Schema(description = "Цена объявления")
    private Integer price;

    @Schema(description = "Заголовок объявления")
    private String title;

    public AdDTO(Integer author, String image, Integer id, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = id;
        this.price = price;
        this.title = title;
    }

    public AdDTO() {

    }
}
