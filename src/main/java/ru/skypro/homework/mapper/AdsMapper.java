package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;

import java.util.List;

@Mapper
public interface AdsMapper {

    @Mapping(source = "ads", target = "results")
    Ads mapToAds(List<Ad> ads);
    @Mapping(source = "results", target = "ads")
    List<Ad> mapFromAds(Ads ads);
}
