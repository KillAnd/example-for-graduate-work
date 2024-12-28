package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);
    private final AdMapperImpl adMapper;
    private final ImageService imageService;

    public AdsServiceImpl(AdRepository adRepository, AdMapperImpl adMapper, ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    public Ads getAllAds() {
        List<Ad> adsList = adRepository.findAll().stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new Ads(adsList.size(), adsList);
    }

    public Ad createAd(CreateOrUpdateAd adProperties,
                       MultipartFile image,
                       String username) throws IOException {
        logger.info("Вошли в метод addAd сервиса AdServiceImpl. " +
                "Получены данные (объект) createAD: {}." +
                "Файл объявления {}." +
                "Имя авторизированного пользователя: {}", adProperties, image.getOriginalFilename(), username);
        Image uploadImage = imageService.uploadImage(image);
        Ad adEntity = adMapper.toAdEntity(adProperties, uploadImage.getFilePath(), username);
        logger.info("Получена сущность: {}", adEntity);
        adRepository.save(adEntity);
        logger.info("Сущность сохранена в БД");

        Ad adEntityBD = adRepository.findAdEntityByImage(uploadImage.getFilePath());

        return adMapper.toAdDto(adEntityBD);
    }

    public ExtendedAd getAdById(Integer id) {
        Ad adEntity = adRepository.findAdByPk(id);
        return adMapper.toExtendedAd(adEntity);
    }

    public Ads getAd(Authentication authentication) {
        List<Ad> myAdsList = adRepository.findAll().stream()
                .filter(adEntity -> adEntity.getUserAd().getUsername().equals(authentication.getName()))
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new Ads(myAdsList.size(), myAdsList);
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
    public Ad updateAd(int id, CreateOrUpdateAd newAd) {
        logger.info("Данные обновлены:{}", adRepository.updateInfoAboutAdByPk(id,
                newAd.getDescription(),
                newAd.getPrice(),
                newAd.getTitle()));
        logger.info("Обновление данных выполнено успешно");

        return adMapper.toAdDto(adRepository.findAdByPk(id));
    }


    @Override
    public byte[] updateAdImage(Integer id, MultipartFile image) throws IOException {

        // Найти объявление по id
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new EntityNotFoundException("Ad with id " + id + " not found");
        }

        Ad ad = adOptional.get();

        // Сохранить новое изображение
        Image imagePath = imageService.uploadImage(image);

        // Обновить ссылку на изображение в объявлении
        ad.setImage(imagePath.getFilePath());

        // Сохранить обновленное объявление в базе данных
        adRepository.save(ad);
        return ad.getImageAd().getData();
    }

    public boolean existId(Integer id) {
        logger.info("Вошли в метод existId сервиса AdServiceImpl. Получен id (int): {}", id);
        return adRepository.existsAdByPk(id);
    }

}