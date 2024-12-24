package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.User;

import java.util.List;

@Component
public class AdMapperImpl implements AdsMapper, ExtendedAdMapper , CreateOrUpdateAdMapper {

    @Override
    public Ads mapToAds(List<Ad> ads) {
        Ads dto = new Ads();
        dto.setCount(ads.size());
        dto.setResults(ads);
        return dto;
    }

    @Override
    public List<Ad> mapFromAds(Ads ads) {
        return ads.getResults();
    }

    @Override
    public ExtendedAd mapToExtendedAd(User user, Ad ad) {
        ExtendedAd dto = new ExtendedAd();
        dto.setPk(ad.getPk());
        dto.setAuthorFirstName(user.getFirstName());
        dto.setAuthorLastName(user.getLastName());
        dto.setDescription(ad.getDescription());
        dto.setEmail(user.getEmail());
        dto.setImage(ad.getImageAd());
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
        ad.setImageAd(extendedAd.getImage());
        return ad;
    }

    @Override
    public CreateOrUpdateAd mapToCreateOrUpdateAd(Ad ad) {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setDescription(ad.getDescription());
        return dto;
    }

    @Override
    public Ad mapFromCreateOrUpdateAd(CreateOrUpdateAd dto) {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        return ad;
    }
}
