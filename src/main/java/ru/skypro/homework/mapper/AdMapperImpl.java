package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.User;

import java.util.List;

/**
 * Класс, реализующий интерфейсы {@link AdsMapper}, {@link ExtendedAdMapper} и {@link CreateOrUpdateAdMapper}.
 * Предоставляет методы для преобразования объектов между DTO и сущностями, связанными с объявлениями.
 */
@Component
public class AdMapperImpl implements AdsMapper, ExtendedAdMapper, CreateOrUpdateAdMapper {

    /**
     * Преобразует список сущностей {@link Ad} в объект {@link Ads}.
     *
     * @param ads Список сущностей {@link Ad}.
     * @return Объект {@link Ads}, содержащий количество объявлений и их список.
     */
    @Override
    public Ads mapToAds(List<Ad> ads) {
        Ads dto = new Ads();
        dto.setCount(ads.size());
        dto.setResults(ads);
        return dto;
    }

    /**
     * Преобразует объект {@link Ads} в список сущностей {@link Ad}.
     *
     * @param ads Объект {@link Ads}.
     * @return Список сущностей {@link Ad}.
     */
    @Override
    public List<Ad> mapFromAds(Ads ads) {
        return ads.getResults();
    }

    /**
     * Преобразует сущности {@link User} и {@link Ad} в объект {@link ExtendedAd}.
     *
     * @param user Сущность {@link User}, представляющая автора объявления.
     * @param ad   Сущность {@link Ad}, представляющая объявление.
     * @return Объект {@link ExtendedAd}, содержащий расширенную информацию об объявлении.
     */
    @Override
    public ExtendedAd mapToExtendedAd(User user, Ad ad) {
        ExtendedAd dto = new ExtendedAd();
        dto.setAdId(ad.getAdId());
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

    /**
     * Преобразует объект {@link ExtendedAd} в сущность {@link Ad}.
     *
     * @param extendedAd Объект {@link ExtendedAd}.
     * @return Сущность {@link Ad}.
     */
    @Override
    public Ad mapFromExtendedAd(ExtendedAd extendedAd) {
        Ad ad = new Ad();
        ad.setAdId(extendedAd.getAdId());
        ad.setTitle(extendedAd.getTitle());
        ad.setPrice(extendedAd.getPrice());
        ad.setDescription(extendedAd.getDescription());
        ad.setImageAd(extendedAd.getImage());
        return ad;
    }

    /**
     * Преобразует сущность {@link Ad} в объект {@link CreateOrUpdateAd}.
     *
     * @param ad Сущность {@link Ad}.
     * @return Объект {@link CreateOrUpdateAd}.
     */
    @Override
    public CreateOrUpdateAd mapToCreateOrUpdateAd(Ad ad) {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        dto.setDescription(ad.getDescription());
        return dto;
    }

    /**
     * Преобразует объект {@link CreateOrUpdateAd} в сущность {@link Ad}.
     *
     * @param dto Объект {@link CreateOrUpdateAd}.
     * @return Сущность {@link Ad}.
     */
    @Override
    public Ad mapFromCreateOrUpdateAd(CreateOrUpdateAd dto) {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setDescription(dto.getDescription());
        ad.setPrice(dto.getPrice());
        return ad;
    }
}
