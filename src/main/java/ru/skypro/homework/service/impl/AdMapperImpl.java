package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.AdsMapper;
import ru.skypro.homework.service.ExtendedAdMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AdMapperImpl implements AdsMapper, ExtendedAdMapper {
    @Override
    public Ads mapToAds(List<Ad> ads) {
        Ads dto = new Ads();
        dto.setCount(ads.size());
        dto.setResults(ads.toArray(new Ad[0]));
        return dto;
    }

    @Override
    public List<Ad> mapFromAds(Ads ads) {
        return new ArrayList<>(Arrays.asList(ads.getResults()));
    }

    @Override
    public ExtendedAd mapToExtendedAd(User user, Ad ad) {
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(ad.getPk());
        dto.setAuthorFirstName(user.getFirstName());
        dto.setAuthorLastName(user.getLastName());
        dto.setDescription(ad.getDescription());
        dto.setEmail(user.getEmail());
        dto.setImage(ad.getImage());
        dto.setPhone(user.getPhone());
        dto.setPrice(ad.getPrice());
        dto.setTitle(ad.getTitle());
        return dto;
    }

    @Override
    public Ad mapFromExtendedAd(ExtendedAd extendedAd) {
        Ad ad = new Ad();
        ad.setPk(extendedAd.getPk());
        ad.setTitle(extendedAd.getTitle());
        ad.setPrice(extendedAd.getPrice());
        ad.setDescription(extendedAd.getDescription());
        ad.setImage(extendedAd.getImage());
        return ad;
    }
}
