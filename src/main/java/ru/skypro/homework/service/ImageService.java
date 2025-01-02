package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;

import java.io.IOException;

public interface ImageService {

    Image uploadImage(MultipartFile imageFile) throws IOException;

    String getExtension(MultipartFile file);

    String getFullPath(String name);
}
