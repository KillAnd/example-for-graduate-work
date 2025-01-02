package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.service.ImageService;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Test
    void testGetImage_Success() throws Exception {
        // Arrange
        String imageName = "test.png";
        String fullPath = "/path/to/images/test.png";
        byte[] imageBytes = new byte[]{1, 2, 3, 4};

        // Мокируем статический метод Files.readAllBytes
        try (MockedStatic<Files> filesMock = Mockito.mockStatic(Files.class)) {
            filesMock.when(() -> Files.readAllBytes(Path.of(fullPath))).thenReturn(imageBytes);

            // Мокируем ImageService
            ImageService imageService = mock(ImageService.class);
            when(imageService.getFullPath(imageName)).thenReturn(fullPath);

            ImageController imageController = new ImageController(imageService);

            // Act
            byte[] result = imageController.getImage(imageName);

            // Assert
            assertArrayEquals(imageBytes, result);
            verify(imageService).getFullPath(imageName);
            filesMock.verify(() -> Files.readAllBytes(Path.of(fullPath)));
        }
    }
}