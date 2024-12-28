package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.exception.CreateOrUpdateCommentException;
@Data
public class CreateOrUpdateComment {

    @Schema(minLength = 8, maxLength = 64, description = "Текст комментария")
    private String textComment;
}
