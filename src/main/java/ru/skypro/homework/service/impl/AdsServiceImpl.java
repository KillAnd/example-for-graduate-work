package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с объявлениями.
 * Предоставляет методы для получения, создания, обновления и удаления объявлений,
 * а также для работы с изображениями объявлений.
 */
@Service
public class AdsServiceImpl implements AdsService {

    private final AdRepository adRepository;
    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);
    private final AdMapperImpl adMapper;
    private final ImageService imageService;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param adRepository Репозиторий для работы с объявлениями.
     * @param adMapper Маппер для преобразования сущностей объявлений.
     * @param imageService Сервис для работы с изображениями.
     */
    public AdsServiceImpl(AdRepository adRepository, AdMapperImpl adMapper, ImageService imageService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
    }

    /**
     * Получение всех объявлений.
     *
     * @return Объект Ads, содержащий количество объявлений и их список.
     */
    public Ads getAllAds() {
        List<Ad> adsList = adRepository.findAll().stream()
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new Ads(adsList.size(), adsList);
    }

    /**
     * Создание нового объявления.
     *
     * @param adProperties Данные для создания объявления.
     * @param image Изображение объявления.
     * @param user Пользователь, создающий объявление.
     * @return Созданное объявление.
     * @throws IOException если произошла ошибка при загрузке изображения.
     */
    public Ad createAd(CreateOrUpdateAd adProperties,
                       MultipartFile image,
                       User user) throws IOException {
        logger.info("Вошли в метод addAd сервиса AdServiceImpl. " +
                "Получены данные (объект) createAD: {}." +
                "Файл объявления {}." +
                "Имя авторизированного пользователя: {}", adProperties, image.getOriginalFilename(), user);
        Image uploadImage = imageService.uploadImage(image);
        Ad adEntity = adMapper.toAdEntity(adProperties, uploadImage.getFilePath(), user);
        adEntity.setImageAd(uploadImage);
        logger.info("Получена сущность: {}", adEntity);
        Ad adEntityBD = adRepository.save(adEntity);
        logger.info("Сущность сохранена в БД");

        return adMapper.toAdDto(adEntityBD);
    }

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param pk Идентификатор объявления.
     * @return Расширенная информация об объявлении.
     */
    public ExtendedAd getAdById(Integer pk) {
        Ad adEntity = adRepository.findAdByPk(pk);
        return adMapper.toExtendedAd(adEntity);
    }

    /**
     * Получение объявлений текущего пользователя.
     *
     * @param authentication Данные аутентификации пользователя.
     * @return Объект Ads, содержащий количество объявлений и их список.
     */
    public Ads getAd(Authentication authentication) {
        List<Ad> myAdsList = adRepository.findAll().stream()
                .filter(adEntity -> adEntity.getUserAd().getUsername().equals(authentication.getName()))
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new Ads(myAdsList.size(), myAdsList);
    }

    /**
     * Удаление объявления по его идентификатору.
     *
     * @param id Идентификатор объявления.
     */
    @Override
    public void deleteAd(int id) {
        adRepository.deleteById(id);
    }

    /**
     * Получение объявлений текущего пользователя.
     *
     * @return Объект Ads, содержащий количество объявлений и их список.
     */
    @Override
    public Ads getMyAds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Ad> myAdsList = adRepository.findAll().stream()
                .filter(ad -> ad.getUserAd().getUsername().equals(auth.getName()))
                .map(adMapper::toAdDto)
                .collect(Collectors.toList());
        return new Ads(myAdsList.size(), List.of(myAdsList.toArray(Ad[]::new)));
    }

    /**
     * Обновление информации об объявлении.
     *
     * @param id Идентификатор объявления.
     * @param newAd Новые данные для обновления объявления.
     * @return Обновленное объявление.
     */
    @Override
    public Ad updateAd(int id, CreateOrUpdateAd newAd) {
        logger.info("Данные обновлены:{}", adRepository.updateInfoAboutAdByPk(id,
                newAd.getDescription(),
                newAd.getPrice(),
                newAd.getTitle()));
        logger.info("Обновление данных выполнено успешно");

        return adMapper.toAdDto(adRepository.findAdByPk(id));
    }

    /**
     * Обновление изображения объявления.
     *
     * @param id Идентификатор объявления.
     * @param image Новое изображение объявления.
     * @throws IOException если произошла ошибка при загрузке изображения.
     */
    @Override
    public void updateAdImage(Integer id, MultipartFile image) throws IOException {
        // Найти объявление по id
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isEmpty()) {
            throw new EntityNotFoundException("Ad with id " + id + " not found");
        }

        Ad ad = adOptional.get();

        // Сохранить новое изображение
        Image imagePath = imageService.uploadImage(image);
        logger.info("Переходим из сервиса объявлений в сервис картинки");
        // Обновить ссылку на изображение в объявлении
        ad.setImage(imagePath.getFilePath());
        logger.info("Путь картинки объявления изменен");
        // Сохранить обновленное объявление в базе данных
        adRepository.save(ad);
        logger.info("Картинка объявления сохранена");
    }

    /**
     * Проверка существования объявления по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return true, если объявление существует, иначе false.
     */
    public boolean existId(Integer id) {
        logger.info("Вошли в метод existId сервиса AdsServiceImpl. Получен id (int): {}", id);
        return adRepository.existsAdByPk(id);
    }
}