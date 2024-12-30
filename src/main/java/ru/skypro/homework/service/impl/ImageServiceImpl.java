package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Реализация сервиса для работы с изображениями.
 * Этот класс предоставляет методы для загрузки изображений и получения их расширений.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Value("${path.to.images.folder}")
    private String imagesDir;

    /**
     * Загружает изображение на сервер и сохраняет его в базе данных.
     *
     * @param imageFile файл изображения
     * @return сохраненное изображение
     * @throws IOException если произошла ошибка при загрузке изображения
     */
    @Override
    public Image uploadImage(MultipartFile imageFile) throws IOException {
        UUID uuid = UUID.randomUUID();

        Image imageAdded = new Image();
        imageAdded.setData(imageFile.getBytes());
        imageAdded.setFileSize(imageFile.getSize());
        imageAdded.setMediaType(imageFile.getContentType());
        logger.info("параметры фото изменены");
        Path filePath = Path.of(imagesDir, uuid + "." + getExtension(imageFile));
        logger.info("Путь изменен");
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        catch (IOException e) {
            logger.info("Потоки не прошли");
        }
        logger.info("Файл успешно сохранён на диск. Полное имя файла: {}", filePath);

        imageAdded.setFilePath(filePath.toString());
        Image savedImage = imageRepository.save(imageAdded);
        logger.info("Изображение загрузилось в базу данных");

        return savedImage;
    }

    /**
     * Возвращает расширение файла.
     *
     * @param file файл, для которого нужно получить расширение
     * @return расширение файла
     * @throws RuntimeException если имя файла не валидно
     */
    @Override
    public String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isBlank() && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        throw new RuntimeException("Название файла не валидно");
    }
}