package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

import java.io.IOException;

public interface AdsService {
    Ads getAllAds();
    Ad createAd(CreateOrUpdateAd adProperties,
                MultipartFile image,
                String username) throws IOException;
    Ads getAd(Authentication authentication);
    void deleteAd(int id);
    Ads getMyAds(int currentUserId);
    Ad updateAd(int id, CreateOrUpdateAd newAd);
    byte[] updateAdImage(Integer id, MultipartFile image) throws IOException;
    boolean existId(Integer id);
    ExtendedAd getAdById(Integer id);
}
