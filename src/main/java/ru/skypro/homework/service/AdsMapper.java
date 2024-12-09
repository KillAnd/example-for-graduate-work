package ru.skypro.homework.service;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;

import java.util.List;

public interface AdsMapper {
    Ads mapToAds(List<Ad> ads);
}
