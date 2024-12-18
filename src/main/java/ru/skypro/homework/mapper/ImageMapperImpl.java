package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.model.Image;

public class ImageMapperImpl implements ImageMapper {
    @Override
    public ImageDTO toDTO(Image image) {
        ImageDTO dto = new ImageDTO();
        dto.setFilePath(image.getFilePath());
        dto.setFileSize(image.getFileSize());
        dto.setMediaType(image.getMediaType());
        return dto;
    }

    @Override
    public Image toEntity(ImageDTO dto) {
        Image image = new Image();
        image.setFilePath(dto.getFilePath());
        image.setFileSize(dto.getFileSize());
        image.setMediaType(dto.getMediaType());
        return image;
    }
}
