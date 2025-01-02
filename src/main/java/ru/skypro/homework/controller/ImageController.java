package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Контроллер для работы с изображениями.
 * Предоставляет REST API для получения изображений по их имени.
 * Все методы доступны по пути "/images".
 */
@RestController
@RequestMapping("/images")
public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param imageService Сервис для работы с изображениями.
     */
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Получение изображения по его имени.
     * Доступно для всех пользователей (без аутентификации).
     *
     * @param name Имя изображения.
     * @return Массив байтов, представляющий изображение.
     * @throws IOException если произошла ошибка при чтении файла изображения.
     */
    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{name}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
    public byte[] getImage(@PathVariable String name) throws IOException {
        logger.info("Попали в метод getImage в ImageController");
        String fullPath = imageService.getFullPath(name);
        logger.info("Выводим картинку по ссылке: {}", fullPath);
        return Files.readAllBytes(Path.of(fullPath));
    }
}
