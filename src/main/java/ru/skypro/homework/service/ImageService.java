package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

    ImageDTO uploadUserImage(String userId, MultipartFile imageFile) throws IOException;
    Path saveToLocalDirectoryUser(User user, MultipartFile imageFile) throws IOException;
    Image saveToDataBasedUser(User user, Path imagePath, MultipartFile imageFile) throws IOException;

    Image getImageUser(User user);

    ImageDTO getImageByUser(String userId);

    ImageDTO uploadAdImage(Integer adId, MultipartFile imageFile) throws IOException;

    Path saveToLocalDirectoryAd(Ad ad, MultipartFile imageFile) throws IOException;

    Image saveToDataBasedAd(Ad ad, Path imagePath, MultipartFile imageFile) throws IOException;

    Image getImageAd(Ad ad);

    ImageDTO getImageByAd(Integer adId);
}
