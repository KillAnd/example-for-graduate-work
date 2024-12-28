package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.model.Comment;

import java.util.List;
@Data
public class Comments {

    @Schema(description = "Общее количество комментариев")
    private Integer count;

    private List<Comment> results;
}
