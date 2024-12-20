package ru.skypro.homework.service.impl;

import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;

import java.util.List;

public class AdsServiceImpl implements AdsService {
    private final AdRepository adRepository;

    private final AdMapperImpl adMapper;

    public AdsServiceImpl(AdRepository adRepository, AdMapperImpl adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    @Override
    public Ads getAllAds() {
        return adMapper.mapToAds(adRepository.findAll());
    }

    @Override
    public CreateOrUpdateAd createAd(CreateOrUpdateAd ad) {
        return adMapper.mapToCreateOrUpdateAd(adRepository.save(adMapper.mapFromCreateOrUpdateAd(ad)));
    }

    @Override
    public ExtendedAd getAd(User user, int id) {
        return adMapper.mapToExtendedAd(user, adRepository.findById(id).get());
    }

    @Override
    public void deleteAd(int id) {
        adRepository.deleteById(id);
    }

    @Override
    public Ads getMyAds(int currentUserId) {
        return adMapper.mapToAds(adRepository.findAdsByAuthor(currentUserId));
    }

    @Override
    public CreateOrUpdateAd updateAd(int id, CreateOrUpdateAd newAd) {
        Ad ad = adMapper.mapFromCreateOrUpdateAd(newAd);
        ad.setPk(id);
        return adMapper.mapToCreateOrUpdateAd(adRepository.save(ad));
    }

    @Override
    public void updateAdImage(int id) {

    }

}
