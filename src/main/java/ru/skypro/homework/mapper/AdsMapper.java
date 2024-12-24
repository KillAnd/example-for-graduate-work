package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;

import java.util.List;

@Mapper
public interface AdsMapper {

    default Ads mapToAds(List<Ad> ads) {
        Ads result = new Ads();
        result.setResults(ads);
        return result;
    }

    default List<Ad> mapFromAds(Ads ads) {
        return ads.getResults();
    }
}
