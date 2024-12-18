package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

    ImageDTO uploadImage(String userId, MultipartFile imageFile) throws IOException;
    Path saveToLocalDirectory(User user, MultipartFile imageFile) throws IOException;
    Image saveToDataBased(User user, Path imagePath, MultipartFile imageFile) throws IOException;
    Image getImageUser(User user);
    ImageDTO getImageByUser(String userId);

}
