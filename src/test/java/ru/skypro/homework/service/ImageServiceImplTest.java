package ru.skypro.homework.service;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.exception.ImageNotFoundException;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.ImageServiceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;


    @InjectMocks
    private ImageServiceImpl imageService;

    // Тест для метода uploadImage
    @Test
    public void testUploadImage_Success() throws IOException {
        // Подготовка данных
        String userId = "test@example.com";
        MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        User user = new User();
        user.setEmail(userId);

        Image image = new Image();
        image.setUser(user);
        image.setFilePath("path/to/image.jpg");
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setFilePath("path/to/image.jpg");
        imageDTO.setFileSize(imageFile.getSize());
        imageDTO.setMediaType(imageFile.getContentType());

        // Мокирование репозиториев и маппера
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        // Выполнение теста
        Image result = imageService.uploadUserImage(userId, imageFile);

        // Проверка результата
        assertNotNull(result);
        assertEquals("path/to/image.jpg", result.getFilePath());
        assertEquals(imageFile.getSize(), result.getFileSize());
        assertEquals(imageFile.getContentType(), result.getMediaType());
    }

    @Test
    public void testUploadImage_UserNotFound() {
        // Подготовка данных
        String userId = "test@example.com";
        MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());

        // Мокирование репозитория
        when(userRepository.findByEmail(userId)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(IllegalArgumentException.class, () -> {
            imageService.uploadUserImage(userId, imageFile);
        });
    }

    // Тест для метода saveToLocalDirectory
    @Test
    public void testSaveToLocalDirectory_Success() throws IOException {
        // Подготовка данных
        String userId = "test@example.com";
        MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        User user = new User();
        user.setEmail(userId);

        // Выполнение теста
        Path result = imageService.saveToLocalDirectoryUser(user, imageFile);

        // Проверка результата
        assertNotNull(result);
        assertTrue(Files.exists(result));
        Files.deleteIfExists(result); // Очистка созданного файла
    }

    // Тест для метода saveToDataBased
    @Test
    public void testSaveToDataBased_Success() throws IOException {
        // Подготовка данных
        String userId = "test@example.com";
        MultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image".getBytes());
        User user = new User();
        user.setEmail(userId);

        Path imagePath = Path.of("path/to/image.jpg");
        Image image = new Image();
        image.setUser(user);
        image.setFilePath(imagePath.toString());
        image.setFileSize(imageFile.getSize());
        image.setMediaType(imageFile.getContentType());
        image.setData(imageFile.getBytes());

        // Мокирование репозитория
        when(imageRepository.save(any(Image.class))).thenReturn(image);

        // Выполнение теста
        Image result = imageService.saveToDataBasedUser(user, imagePath, imageFile);

        // Проверка результата
        assertNotNull(result);
        assertEquals(imagePath.toString(), result.getFilePath());
        assertEquals(imageFile.getSize(), result.getFileSize());
        assertEquals(imageFile.getContentType(), result.getMediaType());
    }

    // Тест для метода getImageUser
    @Test
    public void testGetImageUser_ExistingImage() {
        // Подготовка данных
        String userId = "test@example.com";
        User user = new User();
        user.setEmail(userId);

        Image image = new Image();
        image.setUser(user);

        // Мокирование репозитория
        when(imageRepository.findByUser(user)).thenReturn(Optional.of(image));

        // Выполнение теста
        Image result = imageService.getImageUser(user);

        // Проверка результата
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    public void testGetImageUser_NewImage() {
        // Подготовка данных
        String userId = "test@example.com";
        User user = new User();
        user.setEmail(userId);

        // Мокирование репозитория
        when(imageRepository.findByUser(user)).thenReturn(Optional.empty());

        // Выполнение теста
        Image result = imageService.getImageUser(user);

        // Проверка результата
        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    // Тест для метода getImageByUser
    @Test
    public void testGetImageByUser_Success() {
        // Подготовка данных
        String userId = "test@example.com";
        User user = new User();
        user.setEmail(userId);

        Image image = new Image();
        image.setUser(user);
        image.setFilePath("path/to/image.jpg");

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setFilePath("path/to/image.jpg");

        // Мокирование репозиториев и маппера
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findByUser(user)).thenReturn(Optional.of(image));

        // Выполнение теста
        Image result = imageService.getImageByUser(userId);

        // Проверка результата
        assertNotNull(result);
        assertEquals("path/to/image.jpg", result.getFilePath());
    }

    @Test
    public void testGetImageByUser_UserNotFound() {
        // Подготовка данных
        String userId = "test@example.com";

        // Мокирование репозитория
        when(userRepository.findByEmail(userId)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(IllegalArgumentException.class, () -> {
            imageService.getImageByUser(userId);
        });
    }

    @Test
    public void testGetImageByUser_ImageNotFound() {
        // Подготовка данных
        String userId = "test@example.com";
        User user = new User();
        user.setEmail(userId);

        // Мокирование репозиториев
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));
        when(imageRepository.findByUser(user)).thenReturn(Optional.empty());

        // Выполнение теста и проверка исключения
        assertThrows(ImageNotFoundException.class, () -> {
            imageService.getImageByUser(userId);
        });
    }
}
