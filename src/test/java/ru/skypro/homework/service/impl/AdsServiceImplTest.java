package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.ImageService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdsServiceImplTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private AdMapperImpl adMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private AdsServiceImpl adsService;

    @Test
    void testGetAllAds() {
        // Arrange
        Ad ad1 = new Ad();
        Ad ad2 = new Ad();
        List<Ad> adList = List.of(ad1, ad2);

        when(adRepository.findAll()).thenReturn(adList);
        when(adMapper.toAdDto(ad1)).thenReturn(ad1);
        when(adMapper.toAdDto(ad2)).thenReturn(ad2);

        // Act
        Ads result = adsService.getAllAds();

        // Assert
        assertEquals(2, result.getCount());
        assertEquals(adList, result.getResults());
        verify(adRepository).findAll();
        verify(adMapper, times(2)).toAdDto(any());
    }

    @Test
    void testCreateAd_Success() throws IOException {
        // Arrange
        CreateOrUpdateAd adProperties = new CreateOrUpdateAd();
        MultipartFile image = mock(MultipartFile.class);
        User user = new User();
        Image uploadedImage = new Image();
        Ad adEntity = new Ad();
        Ad savedAd = new Ad();

        when(imageService.uploadImage(image)).thenReturn(uploadedImage);
        when(adMapper.toAdEntity(adProperties, uploadedImage.getFilePath(), user)).thenReturn(adEntity);
        when(adRepository.save(adEntity)).thenReturn(savedAd);
        when(adMapper.toAdDto(savedAd)).thenReturn(savedAd);

        // Act
        Ad result = adsService.createAd(adProperties, image, user);

        // Assert
        assertEquals(savedAd, result);
        verify(imageService).uploadImage(image);
        verify(adMapper).toAdEntity(adProperties, uploadedImage.getFilePath(), user);
        verify(adRepository).save(adEntity);
        verify(adMapper).toAdDto(savedAd);
    }

    @Test
    void testGetAdById_Success() {
        // Arrange
        Integer pk = 1;
        Ad adEntity = new Ad();
        ExtendedAd extendedAd = new ExtendedAd();

        when(adRepository.findAdByPk(pk)).thenReturn(adEntity);
        when(adMapper.toExtendedAd(adEntity)).thenReturn(extendedAd);

        // Act
        ExtendedAd result = adsService.getAdById(pk);

        // Assert
        assertEquals(extendedAd, result);
        verify(adRepository).findAdByPk(pk);
        verify(adMapper).toExtendedAd(adEntity);
    }

    @Test
    void testGetAd_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setUsername("user@example.com");

        Ad ad1 = new Ad();
        ad1.setUserAd(user); // Устанавливаем пользователя для объявления

        Ad ad2 = new Ad();
        ad2.setUserAd(user); // Устанавливаем пользователя для объявления

        List<Ad> adList = List.of(ad1, ad2);

        when(authentication.getName()).thenReturn("user@example.com");
        when(adRepository.findAll()).thenReturn(adList);
        when(adMapper.toAdDto(ad1)).thenReturn(ad1);
        when(adMapper.toAdDto(ad2)).thenReturn(ad2);

        // Act
        Ads result = adsService.getAd(authentication);

        // Assert
        assertEquals(2, result.getCount());
        assertEquals(adList, result.getResults());
        verify(adRepository).findAll();
        verify(adMapper, times(2)).toAdDto(any());
    }

    @Test
    void testDeleteAd_Success() {
        // Arrange
        int id = 1;

        // Act
        adsService.deleteAd(id);

        // Assert
        verify(adRepository).deleteById(id);
    }

    @Test
    void testGetMyAds_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        // Настройка SecurityContextHolder
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Настройка мока для Authentication
        when(authentication.getName()).thenReturn("user@example.com");

        // Инициализация данных
        User user = new User();
        user.setUsername("user@example.com");

        Ad ad1 = new Ad();
        ad1.setUserAd(user); // Устанавливаем пользователя для объявления

        Ad ad2 = new Ad();
        ad2.setUserAd(user); // Устанавливаем пользователя для объявления

        List<Ad> adList = List.of(ad1, ad2);

        when(adRepository.findAll()).thenReturn(adList);
        when(adMapper.toAdDto(ad1)).thenReturn(ad1);
        when(adMapper.toAdDto(ad2)).thenReturn(ad2);

        // Act
        Ads result = adsService.getMyAds();

        // Assert
        assertEquals(2, result.getCount());
        assertEquals(adList, result.getResults());
        verify(adRepository).findAll();
        verify(adMapper, times(2)).toAdDto(any());
    }

    @Test
    void testUpdateAd_Success() {
        // Arrange
        int id = 1;
        CreateOrUpdateAd newAd = new CreateOrUpdateAd();
        Ad updatedAd = new Ad();

        when(adRepository.updateInfoAboutAdByPk(id, newAd.getDescription(), newAd.getPrice(), newAd.getTitle())).thenReturn(1);
        when(adRepository.findAdByPk(id)).thenReturn(updatedAd);
        when(adMapper.toAdDto(updatedAd)).thenReturn(updatedAd);

        // Act
        Ad result = adsService.updateAd(id, newAd);

        // Assert
        assertEquals(updatedAd, result);
        verify(adRepository).updateInfoAboutAdByPk(id, newAd.getDescription(), newAd.getPrice(), newAd.getTitle());
        verify(adRepository).findAdByPk(id);
        verify(adMapper).toAdDto(updatedAd);
    }

    @Test
    void testUpdateAdImage_Success() throws IOException {
        // Arrange
        Integer id = 1;
        MultipartFile image = mock(MultipartFile.class);
        Ad ad = new Ad();
        Image uploadedImage = new Image();

        when(adRepository.findById(id)).thenReturn(Optional.of(ad));
        when(imageService.uploadImage(image)).thenReturn(uploadedImage);

        // Act
        adsService.updateAdImage(id, image);

        // Assert
        verify(adRepository).findById(id);
        verify(imageService).uploadImage(image);
        verify(adRepository).save(ad);
    }

    @Test
    void testUpdateAdImage_NotFound() throws IOException {
        // Arrange
        Integer id = 1;
        MultipartFile image = mock(MultipartFile.class);

        when(adRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> adsService.updateAdImage(id, image));
        verify(adRepository).findById(id);
        verify(imageService, never()).uploadImage(any());
        verify(adRepository, never()).save(any());
    }

    @Test
    void testExistId_True() {
        // Arrange
        Integer id = 1;

        when(adRepository.existsAdByPk(id)).thenReturn(true);

        // Act
        boolean result = adsService.existId(id);

        // Assert
        assertTrue(result);
        verify(adRepository).existsAdByPk(id);
    }

    @Test
    void testExistId_False() {
        // Arrange
        Integer id = 1;

        when(adRepository.existsAdByPk(id)).thenReturn(false);

        // Act
        boolean result = adsService.existId(id);

        // Assert
        assertFalse(result);
        verify(adRepository).existsAdByPk(id);
    }
}