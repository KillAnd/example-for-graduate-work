package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.model.User;

import java.io.IOException;
import java.util.Optional;

public interface AdsService {
    Ads getAllAds();
    CreateOrUpdateAd createAd(CreateOrUpdateAd ad, MultipartFile image);
    ExtendedAd getAd(User user, int id);
    void deleteAd(int id);
    Ads getMyAds(int currentUserId);
    CreateOrUpdateAd updateAd(int id, CreateOrUpdateAd newAd);
    void updateAdImage(Integer id, MultipartFile image) throws IOException;
    boolean isUserOwnerOfAd(Integer userId, ExtendedAd ad);
}
