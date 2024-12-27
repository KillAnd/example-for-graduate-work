package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapperImpl;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;
/**
 * Сервис для работы с объявлениями.
 * Реализует интерфейс {@link AdsService} и предоставляет методы для выполнения операций
 * с объявлениями, таких как создание, получение, обновление, удаление и управление изображениями.
 */
@Service
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    private final AdMapperImpl adMapper;
    private final ImageService imageService;

    /**
     * Конструктор для создания экземпляра AdsServiceImpl.
     *
     * @param adRepository Репозиторий для работы с сущностью {@link Ad}.
     * @param adMapper     Маппер для преобразования между DTO и сущностями.
     * @param imageService Сервис для работы с изображениями.
     */
    public AdsServiceImpl(AdRepository adRepository, AdMapperImpl adMapper, ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    /**
     * Возвращает все объявления.
     *
     * @return Объект {@link Ads}, содержащий список всех объявлений.
     */
    @Override
    public Ads getAllAds() {
        return adMapper.mapToAds(adRepository.findAll());
    }

    /**
     * Создает новое объявление.
     *
     * @param ad    Объект {@link CreateOrUpdateAd}, содержащий данные объявления.
     * @param image Изображение для объявления.
     * @return Объект {@link CreateOrUpdateAd}, представляющий созданное объявление.
     */
    @Override
    public CreateOrUpdateAd createAd(CreateOrUpdateAd ad, MultipartFile image) {
        return adMapper.mapToCreateOrUpdateAd(adRepository.save(adMapper.mapFromCreateOrUpdateAd(ad)));
    }

    /**
     * Возвращает расширенную информацию об объявлении.
     *
     * @param user Пользователь, запрашивающий информацию.
     * @param id   Идентификатор объявления.
     * @return Объект {@link ExtendedAd}, содержащий расширенную информацию об объявлении.
     */
    @Override
    public ExtendedAd getAd(User user, int id) {
        return adMapper.mapToExtendedAd(user, adRepository.findById(id).get());
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id Идентификатор объявления.
     */
    @Override
    public void deleteAd(int id) {
        adRepository.deleteById(id);
    }

    /**
     * Возвращает объявления текущего пользователя.
     *
     * @param currentUserId Идентификатор текущего пользователя.
     * @return Объект {@link Ads}, содержащий список объявлений пользователя.
     */
    @Override
    public Ads getMyAds(int currentUserId) {
        return adMapper.mapToAds(adRepository.findAdsByAuthor(currentUserId));
    }

    /**
     * Обновляет информацию об объявлении.
     *
     * @param id    Идентификатор объявления.
     * @param newAd Объект {@link CreateOrUpdateAd}, содержащий новые данные объявления.
     * @return Объект {@link CreateOrUpdateAd}, представляющий обновленное объявление.
     */
    @Override
    public CreateOrUpdateAd updateAd(int id, CreateOrUpdateAd newAd) {
        Ad ad = adMapper.mapFromCreateOrUpdateAd(newAd);
        ad.setAdId(id);
        return adMapper.mapToCreateOrUpdateAd(adRepository.save(ad));
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id    Идентификатор объявления.
     * @param image Новое изображение для объявления.
     * @throws IOException Если возникает ошибка при загрузке изображения.
     */
    @Override
    public void updateAdImage(Integer id, MultipartFile image) throws IOException {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new EntityNotFoundException("Ad with id " + id + " not found");
        }

        Ad ad = adOptional.get();
        Image imagePath = imageService.uploadAdImage(id, image);
        ad.setImageAd(imagePath);
        adRepository.save(ad);
    }

    /**
     * Проверяет, является ли пользователь владельцем объявления.
     *
     * @param userId Идентификатор пользователя.
     * @param ad     Объект {@link ExtendedAd}, представляющий объявление.
     * @return {@code true}, если пользователь является владельцем объявления, иначе {@code false}.
     */
    @Override
    public boolean isUserOwnerOfAd(Integer userId, ExtendedAd ad) {
        if (ad == null) {
            return false;
        }
        return userId != null && userId.equals(ad.getAdId());
    }
}
