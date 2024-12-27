package ru.skypro.homework.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.model.Ad;

import java.util.List;
@Data
public class Ads {

    @Schema(description = "Общее количество объявлений")
    private Integer count;
    private Ad[] results;

    public Ads(Integer count, Ad[] results) {
        this.count = count;
        this.results = results;
    }

    public Ads() {

    }
}
