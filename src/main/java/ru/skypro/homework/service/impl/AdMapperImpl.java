package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsMapper;
import ru.skypro.homework.service.ExtendedAdMapper;

import java.util.List;

@Service
public class AdMapperImpl implements AdsMapper, ExtendedAdMapper {
    @Override
    public Ads mapToAds(List<Ad> ads) {
        return null;
    }

    @Override
    public ExtendedAd mapToExtendedAd(Ad ad) {
        return null;
    }
}
