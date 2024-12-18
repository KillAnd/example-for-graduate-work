package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.CreateOrUpdateAdException;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdsServiceImplTest {
    @Mock
    private AdRepository adRepository;

    @Mock
    private AdMapperImpl adMapper;

    @InjectMocks
    private AdsServiceImpl adsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAds() {
        //given
        List<Ad> ads = Arrays.asList(new Ad(), new Ad());
        when(adRepository.findAll()).thenReturn(ads);

        Ads expectedAds = new Ads();
        expectedAds.setResults(Arrays.asList(new Ad(), new Ad()));
        when(adMapper.mapToAds(ads)).thenReturn(expectedAds);

        //when
        Ads result = adsService.getAllAds();

        //then
        assertEquals(expectedAds, result);
        verify(adRepository, times(1)).findAll();
        verify(adMapper, times(1)).mapToAds(ads);
    }

    @Test
    public void testCreateAd() {
        //given
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd();
        createOrUpdateAd.setTitle("New Ad");
        createOrUpdateAd.setPrice(10);
        createOrUpdateAd.setDescription("New Ad Description");

        Ad ad = new Ad();
        ad.setTitle("New Ad");
        ad.setPrice(10);
        ad.setDescription("New Ad Description");

        when(adMapper.mapFromCreateOrUpdateAd(createOrUpdateAd)).thenReturn(ad);
        when(adRepository.save(ad)).thenReturn(ad);
        when(adMapper.mapToCreateOrUpdateAd(ad)).thenReturn(createOrUpdateAd);

        //when
        CreateOrUpdateAd result = adsService.createAd(createOrUpdateAd);

        //then
        assertEquals(createOrUpdateAd, result);
        verify(adMapper, times(1)).mapFromCreateOrUpdateAd(createOrUpdateAd);
        verify(adRepository, times(1)).save(ad);
        verify(adMapper, times(1)).mapToCreateOrUpdateAd(ad);
    }

    @Test
    public void testGetAd() {
        //given
        User user = new User();
        int id = 1;
        Ad ad = new Ad();
        ad.setPk(id);

        when(adRepository.findById(id)).thenReturn(Optional.of(ad));

        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(id);
        when(adMapper.mapToExtendedAd(user, ad)).thenReturn(extendedAd);

        //when
        ExtendedAd result = adsService.getAd(user, id);

        //then
        assertEquals(extendedAd, result);
        verify(adRepository, times(1)).findById(id);
        verify(adMapper, times(1)).mapToExtendedAd(user, ad);
    }

    @Test
    public void testDeleteAd() {
        //given
        int id = 1;

        //when
        adsService.deleteAd(id);

        //then
        verify(adRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGetMyAds() {
        //given
        int currentUserId = 1;
        List<Ad> ads = Arrays.asList(new Ad(), new Ad());
        when(adRepository.findAdsByAuthor(currentUserId)).thenReturn(ads);

        Ads expectedAds = new Ads();
        expectedAds.setResults(Arrays.asList(new Ad(), new Ad()));
        when(adMapper.mapToAds(ads)).thenReturn(expectedAds);

        //when
        Ads result = adsService.getMyAds(currentUserId);

        //then
        assertEquals(expectedAds, result);
        verify(adRepository, times(1)).findAdsByAuthor(currentUserId);
        verify(adMapper, times(1)).mapToAds(ads);
    }

    @Test
    public void testUpdateAd() {
        //given
        int id = 1;
        CreateOrUpdateAd newAd = new CreateOrUpdateAd();
        newAd.setTitle("Updated Ad");
        newAd.setPrice(20);
        newAd.setDescription("Updated Ad Description");

        Ad ad = new Ad();
        ad.setPk(id);
        ad.setTitle("Updated Ad");
        ad.setPrice(20);
        ad.setDescription("Updated Ad Description");

        when(adMapper.mapFromCreateOrUpdateAd(newAd)).thenReturn(ad);
        when(adRepository.save(ad)).thenReturn(ad);
        when(adMapper.mapToCreateOrUpdateAd(ad)).thenReturn(newAd);

        //when
        CreateOrUpdateAd result = adsService.updateAd(id, newAd);

        //then
        assertEquals(newAd, result);
        verify(adMapper, times(1)).mapFromCreateOrUpdateAd(newAd);
        verify(adRepository, times(1)).save(ad);
        verify(adMapper, times(1)).mapToCreateOrUpdateAd(ad);
    }

    @Test
    public void testDeleteAd_NonExistentAd() {
        //given
        int nonExistentAdId = 999;
        doThrow(EmptyResultDataAccessException.class).when(adRepository).deleteById(nonExistentAdId);

        //when & Assert
        assertThrows(EmptyResultDataAccessException.class, () -> {
            adsService.deleteAd(nonExistentAdId);
        });

        // Verify
        verify(adRepository, times(1)).deleteById(nonExistentAdId);
    }

    @Test
    public void testGetAd_NonExistentAd() {
        //given
        int nonExistentAdId = 999;
        User user = new User();
        when(adRepository.findById(nonExistentAdId)).thenReturn(Optional.empty());

        //when & Assert
        assertThrows(NoSuchElementException.class, () -> {
            adsService.getAd(user, nonExistentAdId);
        });

        // Verify
        verify(adRepository, times(1)).findById(nonExistentAdId);
    }

    @Test
    public void testCreateAd_InvalidData() {
        //given
        CreateOrUpdateAd invalidAd = new CreateOrUpdateAd();
        invalidAd.setTitle("Hi"); // Слишком короткое название

        //when & Assert
        assertThrows(CreateOrUpdateAdException.class, () -> {
            invalidAd.setTitle(invalidAd.getTitle());
        });

        // Verify
        verify(adRepository, never()).save(any());
    }

    @Test
    public void testUpdateAd_NonExistentAd() {
        //given
        int nonExistentAdId = 999;
        CreateOrUpdateAd newAd = new CreateOrUpdateAd();
        newAd.setTitle("Updated Ad");
        newAd.setPrice(20);
        newAd.setDescription("Updated Ad Description");

        Ad ad = new Ad();
        ad.setPk(nonExistentAdId);
        ad.setTitle("Updated Ad");
        ad.setPrice(20);
        ad.setDescription("Updated Ad Description");

        when(adMapper.mapFromCreateOrUpdateAd(newAd)).thenReturn(ad);
        when(adRepository.save(ad)).thenThrow(EmptyResultDataAccessException.class);

        //when & Assert
        assertThrows(EmptyResultDataAccessException.class, () -> {
            adsService.updateAd(nonExistentAdId, newAd);
        });

        // Verify
        verify(adMapper, times(1)).mapFromCreateOrUpdateAd(newAd);
        verify(adRepository, times(1)).save(ad);
    }

    @Test
    public void testGetMyAds_NoAdsForUser() {
        //given
        int currentUserId = 1;
        when(adRepository.findAdsByAuthor(currentUserId)).thenReturn(Collections.emptyList());

        Ads expectedAds = new Ads();
        expectedAds.setResults(Collections.emptyList());
        when(adMapper.mapToAds(Collections.emptyList())).thenReturn(expectedAds);

        //when
        Ads result = adsService.getMyAds(currentUserId);

        //then
        assertEquals(expectedAds, result);
        verify(adRepository, times(1)).findAdsByAuthor(currentUserId);
        verify(adMapper, times(1)).mapToAds(Collections.emptyList());
    }


}
