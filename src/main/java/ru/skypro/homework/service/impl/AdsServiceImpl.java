package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.ImageDTO;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;

    private final AdMapperImpl adMapper;
    private final ImageService imageService;

    public AdsServiceImpl(AdRepository adRepository, AdMapperImpl adMapper, ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    @Override
    public Ads getAllAds() {
        return adMapper.mapToAds(adRepository.findAll());
    }

    @Override
    public CreateOrUpdateAd createAd(CreateOrUpdateAd ad, MultipartFile image) {
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
    public void updateAdImage(Integer id, MultipartFile image) throws IOException {
        {
            // Найти объявление по id
            Optional<Ad> adOptional = adRepository.findById(id);
            if (adOptional.isEmpty()) {
                throw new EntityNotFoundException("Ad with id " + id + " not found");
            }

            Ad ad = adOptional.get();

            // Сохранить новое изображение
            ImageDTO imagePath = imageService.uploadAdImage(id, image);

            // Обновить ссылку на изображение в объявлении
            ad.setImage(imagePath);

            // Сохранить обновленное объявление в базе данных
            adRepository.save(ad);
        }
    }

    @Override
    public boolean isUserOwnerOfAd(Integer userId, ExtendedAd ad) {
        // Проверяем, что объявление не null
        if (ad == null) {
            return false;
        }

        // Сравниваем идентификатор пользователя с идентификатором владельца объявления
        return userId != null && userId.equals(ad.getPk());
    }

}
