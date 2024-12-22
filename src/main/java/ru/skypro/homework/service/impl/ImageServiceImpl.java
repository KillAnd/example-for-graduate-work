package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.mapper.ImageMapper;
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

@Service
public class ImageServiceImpl implements ImageService {
    private final int BUFFER_SIZE = 1024;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageMapper imageMapper;

    @Value("${path.to.image.folder}")
    private String imageDir;

    @Override
    public ImageDTO uploadUserImage(String userId, MultipartFile imageFile) throws IOException {
        User user = userRepository.findByEmail(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        Path imagePath = saveToLocalDirectoryUser(user, imageFile);
        Image image = saveToDataBasedUser(user, imagePath, imageFile);

        // Преобразуем сущность в DTO
        return imageMapper.toDTO(image);
    }

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
    @Override
    public ImageDTO uploadAdImage(Integer adId, MultipartFile imageFile) throws IOException {
        Ad ad = adRepository.findById(adId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + adId + " not found"));

        Path imagePath = saveToLocalDirectoryAd(ad, imageFile);
        Image image = saveToDataBasedAd(ad, imagePath, imageFile);

        // Преобразуем сущность в DTO
        return imageMapper.toDTO(image);
    }
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
    @Override
    public Image getImageUser(User user) {
        return imageRepository.findByUser(user).orElseGet(() -> {
            Image image = new Image();
            image.setUser(user);
            return image;
        });
    }
    @Override
    public Image getImageAd(Ad ad) {
        return imageRepository.findByAd(ad).orElseGet(() -> {
            Image image = new Image();
            image.setAd(ad);
            return image;
        });
    }

    @Override
    public ImageDTO getImageByUser(String userId) {
        User user = userRepository.findByEmail(userId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + userId + " not found"));

        Image image = imageRepository.findByUser(user).orElseThrow(() ->
                new ImageNotFoundException("Image not found for user: " + userId));

        // Преобразуем сущность в DTO
        return imageMapper.toDTO(image);
    }
    @Override
    public ImageDTO getImageByAd(Integer adId) {
        Ad ad = adRepository.findById(adId).orElseThrow(() ->
                new IllegalArgumentException("User with id " + adId + " not found"));

        Image image = imageRepository.findByAd(ad).orElseThrow(() ->
                new ImageNotFoundException("Image not found for user: " + adId));

        // Преобразуем сущность в DTO
        return imageMapper.toDTO(image);
    }
    private static String getExtensions(String fileName) {

        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
