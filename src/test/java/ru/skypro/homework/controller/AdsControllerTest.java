package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsControllerTest {

    @Mock
    private AdsService adsService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdsController adsController;

    @Test
    void testGetAllAds_Success() {
        // Arrange
        Ads ads = new Ads();
        when(adsService.getAllAds()).thenReturn(ads);

        // Act
        ResponseEntity<Ads> response = adsController.getAllAds();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ads, response.getBody());
        verify(adsService).getAllAds();
    }

    @Test
    void testAddAd_Success() throws IOException {
        // Arrange
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        MultipartFile image = mock(MultipartFile.class);
        User user = new User();
        Ad createdAd = new Ad();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenReturn(user);
        when(adsService.createAd(ad, image, user)).thenReturn(createdAd);

        // Act
        ResponseEntity<Ad> response = adsController.addAd(ad, image);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdAd, response.getBody());
        verify(adsService).createAd(ad, image, user);
    }

    @Test
    void testAddAd_Unauthorized() throws IOException {
        // Arrange
        CreateOrUpdateAd ad = new CreateOrUpdateAd();
        MultipartFile image = mock(MultipartFile.class);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com");
        when(userRepository.findByUsername("user@example.com")).thenReturn(null);

        // Act
        ResponseEntity<Ad> response = adsController.addAd(ad, image);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(adsService, never()).createAd(any(), any(), any());
    }

    @Test
    void testGetAd_Success() {
        // Arrange
        Integer id = 1;
        ExtendedAd extendedAd = new ExtendedAd();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Настройка моков для SecurityContextHolder и Authentication
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com"); // Убедитесь, что имя пользователя не null

        // Настройка моков для adsService
        when(adsService.existId(id)).thenReturn(true);
        when(adsService.getAdById(id)).thenReturn(extendedAd);

        // Act
        ResponseEntity<ExtendedAd> response = adsController.getAd(id, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(extendedAd, response.getBody());
        verify(adsService).getAdById(id);
    }

    @Test
    void testGetAd_NotFound() {
        // Arrange
        Integer id = 1;
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Настройка моков для SecurityContextHolder и Authentication
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com"); // Убедитесь, что имя пользователя не null

        // Настройка мока для adsService
        when(adsService.existId(id)).thenReturn(false);

        // Act
        ResponseEntity<ExtendedAd> response = adsController.getAd(id, authentication);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(adsService, never()).getAdById(any());
    }

    @Test
    void testDeleteAd_Success() {
        // Arrange
        Integer id = 1;
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(adsService.existId(id)).thenReturn(true);

        // Act
        ResponseEntity<Object> response = adsController.deleteAd(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adsService).deleteAd(id);
    }

    @Test
    void testUpdateAd_Success() {
        // Arrange
        Integer id = 1;
        CreateOrUpdateAd newAd = new CreateOrUpdateAd();
        Ad updatedAd = new Ad();
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Настройка моков для SecurityContextHolder и Authentication
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com"); // Убедитесь, что имя пользователя не null

        // Настройка моков для adsService
        when(adsService.existId(id)).thenReturn(true);
        when(adsService.updateAd(id, newAd)).thenReturn(updatedAd);

        // Act
        ResponseEntity<Ad> response = adsController.updateAd(id, newAd, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAd, response.getBody());
        verify(adsService).updateAd(id, newAd);
    }

    @Test
    void testGetUserAds_Success() {
        // Arrange
        Ads ads = new Ads();
        when(adsService.getMyAds()).thenReturn(ads);

        // Act
        ResponseEntity<Ads> response = adsController.getUserAds();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ads, response.getBody());
        verify(adsService).getMyAds();
    }

    @Test
    void testUpdateAdImage_Success() throws IOException {
        // Arrange
        Integer id = 1;
        MultipartFile image = mock(MultipartFile.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Настройка моков для SecurityContextHolder и Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com"); // Убедитесь, что имя пользователя не null

        // Настройка мока для adsService
        when(adsService.existId(id)).thenReturn(true);

        // Act
        ResponseEntity<?> response = adsController.updateAdImage(id, image);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adsService).updateAdImage(id, image);
    }

    @Test
    void testUpdateAdImage_NotFound() throws IOException {
        // Arrange
        Integer id = 1;
        MultipartFile image = mock(MultipartFile.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Настройка моков для SecurityContextHolder и Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user@example.com"); // Убедитесь, что имя пользователя не null

        // Настройка мока для adsService
        when(adsService.existId(id)).thenReturn(false);

        // Act
        ResponseEntity<?> response = adsController.updateAdImage(id, image);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(adsService, never()).updateAdImage(any(), any());
    }
}