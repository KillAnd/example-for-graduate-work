package ru.skypro.homework.service.impl;

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

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Сервис для работы с изображениями.
 * Реализует интерфейс {@link ImageService} и предоставляет методы для загрузки, сохранения
 * и получения изображений, связанных с пользователями и объявлениями.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private final int BUFFER_SIZE = 1024;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Value("${path.to.image.folder}")
    private String imageDir;

    /**
     * Загружает изображение для пользователя.
     *
     * @param userId    Идентификатор пользователя.
     * @param imageFile Файл изображения.
     * @return Объект {@link Image}, представляющий загруженное изображение.
     * @throws IOException Если возникает ошибка при загрузке изображения.
     */
    @Override
    public Image uploadUserImage(String userId, MultipartFile imageFile) throws IOException {
        User user = userRepository.findByEmail(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        Path imagePath = saveToLocalDirectoryUser(user, imageFile);
        Image image = saveToDataBasedUser(user, imagePath, imageFile);

        return image;
    }

    /**
     * Сохраняет изображение пользователя в локальной директории.
     *
     * @param user      Пользователь, для которого сохраняется изображение.
     * @param imageFile Файл изображения.
     * @return Путь к сохраненному изображению.
     * @throws IOException Если возникает ошибка при сохранении изображения.
     */
    @Override
    public Path saveToLocalDirectoryUser(User user, MultipartFile imageFile) throws IOException {
        Path imagePath = Path
                .of(imageDir, user.getEmail() + "." + getExtensions(imageFile.getOriginalFilename()));
        Files.createDirectories(imagePath.getParent());
        Files.deleteIfExists(imagePath);
        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(imagePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        ) {
            bis.transferTo(bos);
        }

        return imagePath;
    }

    /**
     * Сохраняет изображение пользователя в базе данных.
     *
     * @param user      Пользователь, для которого сохраняется изображение.
     * @param imagePath Путь к изображению в локальной директории.
     * @param imageFile Файл изображения.
     * @return Объект {@link Image}, представляющий сохраненное изображение.
     * @throws IOException Если возникает ошибка при сохранении изображения.
     */
    @Override
    public Image saveToDataBasedUser(User user, Path imagePath, MultipartFile imageFile) throws IOException {
        Image image = getImageUser(user);
        image.setUser(user);
        image.setFilePath(imagePath.toString());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());

        return imageRepository.save(image);
    }

    /**
     * Загружает изображение для объявления.
     *
     * @param adId      Идентификатор объявления.
     * @param imageFile Файл изображения.
     * @return Объект {@link Image}, представляющий загруженное изображение.
     * @throws IOException Если возникает ошибка при загрузке изображения.
     */
    @Override
    public Image uploadAdImage(Integer adId, MultipartFile imageFile) throws IOException {
        Ad ad = adRepository.findById(adId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + adId + " not found"));

        Path imagePath = saveToLocalDirectoryAd(ad, imageFile);
        Image image = saveToDataBasedAd(ad, imagePath, imageFile);

        return image;
    }

    /**
     * Сохраняет изображение объявления в локальной директории.
     *
     * @param ad        Объявление, для которого сохраняется изображение.
     * @param imageFile Файл изображения.
     * @return Путь к сохраненному изображению.
     * @throws IOException Если возникает ошибка при сохранении изображения.
     */
    @Override
    public Path saveToLocalDirectoryAd(Ad ad, MultipartFile imageFile) throws IOException {
        Path imagePath = Path
                .of(imageDir, ad.getAuthor() + "." + getExtensions(imageFile.getOriginalFilename()));
        Files.createDirectories(imagePath.getParent());
        Files.deleteIfExists(imagePath);
        try (
                InputStream is = imageFile.getInputStream();
                OutputStream os = Files.newOutputStream(imagePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
                BufferedOutputStream bos = new BufferedOutputStream(os, BUFFER_SIZE);
        ) {
            bis.transferTo(bos);
        }

        return imagePath;
    }

    /**
     * Сохраняет изображение объявления в базе данных.
     *
     * @param ad        Объявление, для которого сохраняется изображение.
     * @param imagePath Путь к изображению в локальной директории.
     * @param imageFile Файл изображения.
     * @return Объект {@link Image}, представляющий сохраненное изображение.
     * @throws IOException Если возникает ошибка при сохранении изображения.
     */
    @Override
    public Image saveToDataBasedAd(Ad ad, Path imagePath, MultipartFile imageFile) throws IOException {
        Image image = getImageAd(ad);
        image.setAd(ad);
        image.setFilePath(imagePath.toString());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());

        return imageRepository.save(image);
    }

    /**
     * Возвращает изображение пользователя.
     *
     * @param user Пользователь, для которого запрашивается изображение.
     * @return Объект {@link Image}, представляющий изображение пользователя.
     */
    @Override
    public Image getImageUser(User user) {
        return imageRepository.findByUser(user).orElseGet(() -> {
            Image image = new Image();
            image.setUser(user);
            return image;
        });
    }

    /**
     * Возвращает изображение объявления.
     *
     * @param ad Объявление, для которого запрашивается изображение.
     * @return Объект {@link Image}, представляющий изображение объявления.
     */
    @Override
    public Image getImageAd(Ad ad) {
        return imageRepository.findByAd(ad).orElseGet(() -> {
            Image image = new Image();
            image.setAd(ad);
            return image;
        });
    }

    /**
     * Возвращает изображение пользователя по его идентификатору.
     *
     * @param userId Идентификатор пользователя.
     * @return Объект {@link Image}, представляющий изображение пользователя.
     * @throws IllegalArgumentException Если пользователь не найден.
     * @throws ImageNotFoundException   Если изображение не найдено.
     */
    @Override
    public Image getImageByUser(String userId) {
        User user = userRepository.findByEmail(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        Image image = imageRepository.findByUser(user).orElseThrow(() ->
                new ImageNotFoundException("Image not found for user: " + userId));

        return image;
    }

    /**
     * Возвращает изображение объявления по его идентификатору.
     *
     * @param adId Идентификатор объявления.
     * @return Объект {@link Image}, представляющий изображение объявления.
     * @throws IllegalArgumentException Если объявление не найден.
     * @throws ImageNotFoundException   Если изображение не найдено.
     */
    @Override
    public Image getImageByAd(Integer adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + adId + " not found"));

        Image image = imageRepository.findByAd(ad).orElseThrow(() ->
                new ImageNotFoundException("Image not found for user: " + adId));

        return image;
    }

    /**
     * Возвращает расширение файла.
     *
     * @param fileName Имя файла.
     * @return Расширение файла.
     */
    private static String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
