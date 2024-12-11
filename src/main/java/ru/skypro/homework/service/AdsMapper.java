package ru.skypro.homework.service;

import org.mapstruct.Mapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;

import java.util.List;

@Mapper
public interface AdsMapper {
    Ads mapToAds(List<Ad> ads);
    List<Ad> mapFromAds(Ads ads);
}
