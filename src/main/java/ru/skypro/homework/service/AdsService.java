package ru.skypro.homework.service;

import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

public interface AdsService {
    Ads getAllAds();
    CreateOrUpdateAd createAd(CreateOrUpdateAd ad);
    ExtendedAd getAd(User user, int id);
    void deleteAd(int id);
    Ads getMyAds(int currentUserId);
    CreateOrUpdateAd updateAd(int id, CreateOrUpdateAd newAd);
    void updateAdImage(int id);
}
