package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.model.Image;

public interface ImageMapper {

    ImageDTO toDTO(Image image);
    Image toEntity(ImageDTO dto);

}
