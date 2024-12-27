package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import static ru.skypro.homework.dto.Role.ADMIN;

/**
 * REST-контроллер для управления объявлениями.
 * Предоставляет эндпоинты для выполнения операций с объявлениями, таких как:
 * <ul>
 *     <li>Получение всех объявлений</li>
 *     <li>Добавление нового объявления</li>
 *     <li>Получение информации о конкретном объявлении</li>
 *     <li>Удаление объявления</li>
 *     <li>Обновление информации об объявлении</li>
 *     <li>Получение объявлений текущего пользователя</li>
 *     <li>Обновление изображения объявления</li>
 * </ul>
 * Все операции, связанные с изменением данных, доступны только авторизованным пользователям.
 */
@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;
    private final AuthService authService;
    private final UserService userService;

    /**
     * Конструктор для создания экземпляра AdsController.
     *
     * @param adsService  Сервис для работы с объявлениями.
     * @param authService Сервис для аутентификации пользователей.
     * @param userService Сервис для работы с пользователями.
     */
    public AdsController(AdsService adsService, AuthService authService, UserService userService) {
        this.adsService = adsService;
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Получает все объявления.
     *
     * @return {@link ResponseEntity} с объектом {@link Ads}, содержащим список всех объявлений.
     */
    @GetMapping()
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    /**
     * Добавляет новое объявление.
     *
     * @param ad    Объект {@link CreateOrUpdateAd}, содержащий данные объявления.
     * @param image Изображение для объявления.
     * @param user  Аутентифицированный пользователь.
     * @return {@link ResponseEntity} с созданным объявлением и статусом {@link HttpStatus#CREATED}.
     *         Если пользователь не аутентифицирован, возвращает статус {@link HttpStatus#UNAUTHORIZED}.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateOrUpdateAd> addAd(@RequestPart("properties") CreateOrUpdateAd ad,
                                                  @RequestPart("image") MultipartFile image,
                                                  @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CreateOrUpdateAd createdAd = adsService.createAd(ad, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    /**
     * Получает информацию об объявлении по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return {@link ResponseEntity} с объектом {@link ExtendedAd}, содержащим информацию об объявлении.
     *         Если пользователь не найден, возвращает статус {@link HttpStatus#UNAUTHORIZED}.
     *         Если объявление не найдено, возвращает статус {@link HttpStatus#NOT_FOUND}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        Optional<User> user = userService.findUserById(id.longValue());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (id == user.get().getId()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adsService.getAd(user.get(), id));
    }

    /**
     * Удаляет объявление по его идентификатору.
     * Доступно только для пользователей с ролью {@link Role#USER} или {@link Role#ADMIN}.
     *
     * @param id Идентификатор объявления.
     * @return {@link ResponseEntity} с пустым телом и статусом {@link HttpStatus#NO_CONTENT}, если удаление прошло успешно.
     *         Если объявление не найдено, возвращает статус {@link HttpStatus#NOT_FOUND}.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAd(@PathVariable int id) {
        User user = new User();
        ExtendedAd ad = adsService.getAd(user, id);
        if (user.getRole() == ADMIN) {
            adsService.deleteAd(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            Ads ads = adsService.getMyAds(id);
            if (!ads.getResults().contains(ad)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                adsService.deleteAd(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
    }

    /**
     * Обновляет информацию об объявлении.
     * Доступно только для пользователей с ролью {@link Role#USER} или {@link Role#ADMIN}.
     *
     * @param id    Идентификатор объявления.
     * @param newAd Новые данные для объявления.
     * @param user  Аутентифицированный пользователь.
     * @return {@link ResponseEntity} с обновленным объявлением.
     *         Если объявление не найдено, возвращает статус {@link HttpStatus#NOT_FOUND}.
     *         Если пользователь не имеет прав на обновление, возвращает статус {@link HttpStatus#FORBIDDEN}.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<CreateOrUpdateAd> updateAd(@PathVariable int id, @RequestBody CreateOrUpdateAd newAd,
                                                     @AuthenticationPrincipal User user) {
        ResponseEntity<ExtendedAd> oldAdResponse = getAd(id);
        if (oldAdResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ExtendedAd oldAd = oldAdResponse.getBody();
        if (user.getRole() == ADMIN || adsService.isUserOwnerOfAd(user.getId(), oldAd)) {
            CreateOrUpdateAd updatedAd = adsService.updateAd(id, newAd);
            return ResponseEntity.ok(updatedAd);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Получает объявления текущего авторизованного пользователя.
     *
     * @param user Аутентифицированный пользователь.
     * @return {@link ResponseEntity} с объектом, содержащим список объявлений пользователя.
     *         Если пользователь не аутентифицирован, возвращает статус {@link HttpStatus#UNAUTHORIZED}.
     */
    @GetMapping("/me")
    public ResponseEntity<Object> getUserAds(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok(adsService.getMyAds(user.getId()));
        }
    }

    /**
     * Обновляет изображение объявления.
     * Доступно только для пользователей с ролью {@link Role#USER} или {@link Role#ADMIN}.
     *
     * @param id    Идентификатор объявления.
     * @param image Новое изображение для объявления.
     * @param user  Аутентифицированный пользователь.
     * @return {@link ResponseEntity} с пустым телом и статусом {@link HttpStatus#OK}, если обновление прошло успешно.
     *         Если пользователь не аутентифицирован, возвращает статус {@link HttpStatus#UNAUTHORIZED}.
     *         Если объявление не найдено, возвращает статус {@link HttpStatus#NOT_FOUND}.
     *         Если пользователь не имеет прав на обновление, возвращает статус {@link HttpStatus#FORBIDDEN}.
     * @throws IOException Если произошла ошибка при обработке изображения.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/image")
    public ResponseEntity<Void> updateAdImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image,
                                              @AuthenticationPrincipal User user) throws IOException {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        ExtendedAd ad = adsService.getAd(user, id);
        if (ad != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (user.getRole() == Role.ADMIN || ad.getEmail().equals(user.getEmail())) {
            adsService.updateAdImage(id, image);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
