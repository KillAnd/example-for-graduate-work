package ru.skypro.homework.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.List;

/**
 * Реализация маппера для преобразования между сущностью Ad и её DTO представлениями.
 * Этот класс предоставляет методы для преобразования объектов CreateOrUpdateAd, Ads и ExtendedAd
 * в сущность Ad и наоборот.
 */
@Component
public class AdMapperImpl {

    Logger logger = LoggerFactory.getLogger(AdMapperImpl.class);

    @Value("${base.url.for.images}")
    private String baseURL;

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    /**
     * Конструктор для инициализации маппера.
     *
     * @param userRepository репозиторий для работы с пользователями
     * @param adRepository   репозиторий для работы с объявлениями
     */
    public AdMapperImpl(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    /**
     * Преобразует объект CreateOrUpdateAd в сущность Ad.
     *
     * @param ad      объект CreateOrUpdateAd, содержащий данные для создания или обновления объявления
     * @param filePath путь к файлу изображения
     * @param user    пользователь, создавший объявление
     * @return сущность Ad
     * @throws AdNotFoundException если переданный объект ad равен null
     */
    public Ad toAdEntity(CreateOrUpdateAd ad, String filePath, User user) {
        if (ad == null) {
            throw new AdNotFoundException("Переданный объект Ad is null");
        }
        Ad adEntity = new Ad();
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setImage(filePath);
        adEntity.setAuthor(user.getId());
        adEntity.setUserAd(user);
        return adEntity;
    }

    /**
     * Преобразует объект CreateOrUpdateAd в сущность Ad.
     *
     * @param ad      объект CreateOrUpdateAd, содержащий данные для создания или обновления объявления
     * @param filePath путь к файлу изображения
     * @param username имя пользователя, создавшего объявление
     * @return сущность Ad
     * @throws AdNotFoundException если переданный объект ad равен null
     */
    public Ad toAd(CreateOrUpdateAd ad, String filePath, String username) {
        if (ad == null) {
            throw new AdNotFoundException("Переданный объект Ad is null");
        }
        Ad adEntity = new Ad();
        adEntity.setPrice(ad.getPrice());
        adEntity.setTitle(ad.getTitle());
        adEntity.setDescription(ad.getDescription());
        adEntity.setImage(filePath.replace('\\', '/'));
        adEntity.setUserAd(userRepository.findByUsername(username));
        return adEntity;
    }

    /**
     * Преобразует сущность Ad в объект Ad.
     *
     * @param adEntity сущность Ad
     * @return объект Ad
     * @throws AdNotFoundException если переданный объект adEntity равен null
     */
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

    /**
     * Преобразует сущность Ad в объект ExtendedAd.
     *
     * @param adEntity сущность Ad
     * @return объект ExtendedAd
     * @throws AdNotFoundException если переданный объект adEntity равен null
     */
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
        extendedAd.setImage(adEntity.getImage().replace('\\', '/'));
        extendedAd.setPhone(adEntity.getUserAd().getPhone());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());
        return extendedAd;
    }

    /**
     * Преобразует список сущностей Ad в объект Ads.
     *
     * @param ads список сущностей Ad
     * @return объект Ads
     */
    public Ads mapToAds(List<Ad> ads) {
        Ads dto = new Ads();
        dto.setCount(ads.size());
        dto.setResults(ads);
        return dto;
    }
}