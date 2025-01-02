package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.ImageRepository;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile(
                "testImage",
                "testImage.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        imageService.imagesDir = "/images"; // Устанавливаем значение imagesDir
    }

    @Test
    void uploadImage_Success() throws IOException {
        // Arrange
        Image savedImage = new Image();
        savedImage.setFilePath("path/to/image/testImage.jpg");
        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);

        // Act
        Image result = imageService.uploadImage(mockFile);

        // Assert
        assertNotNull(result);
        assertEquals("path/to/image/testImage.jpg", result.getFilePath());
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void uploadImage_IOException_ThrowsRuntimeException() throws IOException {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> imageService.uploadImage(mockFile));
    }

    @Test
    void getExtension_ValidFileName_ReturnsExtension() {
        // Arrange
        String expectedExtension = "jpg";

        // Act
        String result = imageService.getExtension(mockFile);

        // Assert
        assertEquals(expectedExtension, result);
    }

    @Test
    void getExtension_InvalidFileName_ThrowsException() {
        // Arrange
        MultipartFile invalidFile = new MockMultipartFile(
                "invalidFile",
                "",
                "image/jpeg",
                new byte[0]
        );

        // Act & Assert
        assertThrows(RuntimeException.class, () -> imageService.getExtension(invalidFile));
    }

    @Test
    void getFullPath_ValidName_ReturnsFullPath() {
        // Arrange
        String imageName = "testImage.jpg";
        String expectedPath = "/images/testImage.jpg";

        // Act
        String result = imageService.getFullPath(imageName);

        // Assert
        assertEquals(expectedPath, result);
    }
}