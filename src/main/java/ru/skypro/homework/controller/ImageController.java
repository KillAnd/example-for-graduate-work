package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.Paths.get;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final UserRepository userRepository;

    private final AdRepository adRepository;

    @Value("${path.to.images.folder}")
    private String imagesDir;

    public ImageController(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{name}",
            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
    public byte[] getImage(@PathVariable String name) throws IOException {
        logger.info("попали в метод getImage в image controller");
        String fullPath = imagesDir + '/' + name;
        logger.info("выводим картинку по ссылке {}", fullPath);
        File file = new File(fullPath);
        if (file.exists()) {
            logger.info("грузим файл по пути: {}", fullPath);
            return Files.readAllBytes(Path.of(fullPath));
        }
        return null;
    }

//    @PreAuthorize("permitAll()")
//    @GetMapping(value = "/images/ads/{id}",
//            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, "image/*"})
//    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) throws IOException {
//        Ad ad = adRepository.findAdByPk(id);
//        if (ad!=null) {
//            String path = ad.getImage();
//            if (!Files.exists(Paths.get(path))) {
//                logger.error("Файл не найден по пути: {}", path);
//                throw new IOException("Файл не найден");
//            }
//            byte[] image = Files.readAllBytes(get(path));
//            logger.info("грузим файл по пути: {}", path);
//            return ResponseEntity.ok(image);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
}
