package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import static ru.skypro.homework.dto.Role.ADMIN;

@RestController
@RequestMapping("/ads")
public class AdsController {
    Logger logger = LoggerFactory.getLogger(AdsController.class);

    private final AdsService adsService;
    private final UserRepository userRepository;


    public AdsController(AdsService adsService, UserRepository userRepository) {
        this.adsService = adsService;
        this.userRepository = userRepository;
    }

    //Получение всех объявлений
    @GetMapping()
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    //Добавление объявления
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd ad,
                                    @RequestPart("image") MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Зашли в метод по добавлению объявления");
        User user = userRepository.findByUsername(authentication.getName());
        // Проверка аутентификации пользователя
        if (user == null) {
            logger.warn("Пользователь не аутентифицирован");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Проверка, был ли передан файл
        if (image == null || image.isEmpty()) {
            logger.warn("Файл изображения не был передан");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Создание объявления
        Ad createdAd;
        try {
            createdAd = adsService.createAd(ad, image, user);
            logger.info("Объявление успешно создано: {}", createdAd.getTitle());
        } catch (IOException e) {
            logger.error("Ошибка при создании объявления", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAd);
    }

    //Получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adsService.existId(id)) {
            return ResponseEntity.ok(adsService.getAdById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Удаление объявления
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAd(@PathVariable int id, Authentication authentication) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adsService.existId(id)) {
            adsService.deleteAd(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Обновление информации об объявлении
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody CreateOrUpdateAd newAd,
                                      Authentication authentication) {
        // Получение объявления, которое необходимо обновить
        ResponseEntity<ExtendedAd> oldAdResponse = getAd(id);

        // Проверка, существует ли объявление
        if (oldAdResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Ad updatedAd = adsService.updateAd(id, newAd);
        return ResponseEntity.ok(updatedAd);
    }


    // Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public ResponseEntity<Object> getUserAds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            User user = userRepository.findByUsername(authentication.getName());
            return ResponseEntity.ok(adsService.getMyAds(user.getId()));
        }
    }

    // Обновление картинки объявления
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}/image")
    public ResponseEntity<byte[]> updateAdImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image,
                                              Authentication authentication) throws IOException {
        logger.info("Метод addAds, класса AdController. Приняты: (int) id {}. Изображение объявления{}",
                id, image.getOriginalFilename());
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adsService.existId(id)) {
            byte[] imageArrayBytes = adsService.updateAdImage(id, image);
            logger.info("Получен массив байт в контроллер: {}", imageArrayBytes[0]);
            return new ResponseEntity<>(imageArrayBytes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
