package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

@Component
public class AdMapperImpl {
    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public AdMapperImpl(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    public Ad toAdEntity(CreateOrUpdateAd ad,
                               String filePath,
                               String username) {
        if (ad == null) {
            throw new AdNotFoundException("Переданный объект Ad is null");
        }
        Ad adEntity = new Ad();
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setImage(filePath);
        adEntity.setUserAd(userRepository.findByUsername(username));
        return adEntity;
    }

    public Ad toAd(CreateOrUpdateAd ad,
                   String filePath,
                   String username) {
        if (ad == null) {
            throw new AdNotFoundException("Переданный объект Ad is null");
        }
        Ad adEntity = new Ad();
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setImage(filePath);
        adEntity.setUserAd(userRepository.findByUsername(username));
        return adEntity;
    }

    public Ad toAdDto(Ad adEntity) {
        if (adEntity == null) {
            throw new AdNotFoundException("Переданный объект AdEntity is null");
        }
        Ad ad = new Ad();
        ad.setAuthor(adEntity.getUserAd().getId());
        ad.setImage(adEntity.getImage());
        ad.setPk(adEntity.getPk());
        ad.setPrice(adEntity.getPrice());
        ad.setTitle(adEntity.getTitle());
        return ad;
    }

    public ExtendedAd toExtendedAd(Ad adEntity) {
        if (adEntity == null) {
            throw new AdNotFoundException("Переданный объект AdEntity is null");
        }
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getPk());
        extendedAd.setAuthorFirstName(adEntity.getUserAd().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getUserAd().getLastName());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setEmail(adEntity.getUserAd().getUsername());
        extendedAd.setImage(adEntity.getImage());
        extendedAd.setPhone(adEntity.getUserAd().getPhone());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }

    public Ads mapToAds(List<Ad> ads) {
        Ads dto = new Ads();
        dto.setCount(ads.size());
        dto.setResults(ads);
        return dto;
    }
}
