package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.util.Arrays;
import java.util.function.Predicate;

import static ru.skypro.homework.dto.Role.ADMIN;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;
    private final AuthService authService;
    private UserService userService;


    public AdsController(AdsService adsService, AuthService authService) {
        this.adsService = adsService;
        this.authService = authService;
    }

    //Получение всех объявлений
    @GetMapping()
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    //Добавление объявления
    @PostMapping()
    public ResponseEntity<Object> createAd(@RequestBody Ad ad, @RequestBody String image) {
        User user = userService.findUserById(currentUserId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok(adsService.createAd());
        }
    }

    //Получение информации об объявлении
    @GetMapping("/{id}")
    public ResponseEntity<Ad> getAd(@PathVariable long id) {
        User user = userService.findUserById(currentUserId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (id == 0) { // тут надо будет вставить запрос на поиск такого id в базе (или в сервисном методе эту проверку делать)
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(adsService.getAd(id));
    }

    //Удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAd(@PathVariable long id) {
        User user = new User();
        // поиск объявления
        ResponseEntity<Ad> ad = getAd(id);
        // проверка, является ли пользователь админом
        if (user.getRole() == ADMIN) {
            adsService.deleteAd(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else { // ищем объявление среди объявлений пользователя
            Ads ads = adsService.getMyAds();
            if (!ads.getResults.contains(ad)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                adsService.deleteAd(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

        }
    }

    //Обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable long id, @RequestBody Ad newAd,
                                       @AuthenticationPrincipal User user) {
        // Получение объявления, которое необходимо обновить
        ResponseEntity<Ad> oldAdResponse = getAd(id);

        // Проверка, существует ли объявление
        if (oldAdResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Ad oldAd = oldAdResponse.getBody();

        // Проверка, является ли пользователь админом или владельцем объявления
        if (user.getRole() == ADMIN || adsService.isUserOwnerOfAd(user.getId(), oldAd)) {
            Ad updatedAd = adsService.updateAd(id, newAd);
            return ResponseEntity.ok(updatedAd);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public ResponseEntity<Object> getUserAds() {
        User user = userService.findUserById(currentUserId);
        // Проверка, что пользователь найден
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.ok(adsService.getMyAds());
        }
    }

    //Обновление картинки объявления
    @PatchMapping("/ads/{id}/image")
    public ResponseEntity<Void> updateAdImage(@PathVariable long id, @RequestParam("image") MultipartFile image) {
        adsService.updateAdImage(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
