package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.service.AuthService;

@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;
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
    public ResponseEntity<Object> getAd(@PathVariable long id) {
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
        // поиск объявления
        Ad ad = getAd(id);
        // проверка, является ли пользователь админом
        if (user.getRole == "ADMIN") {
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
    public ResponseEntity<Object> updateAd(@PathVariable long id, @RequestBody Ad newAd) {
        // получение объявления, которое необходимо обновить
        Ad oldAd = getAd(id);
        // проверка, является ли пользователь админом
        if (user.getRole == "ADMIN") {
            adsService.updateAd(id, newAd);
            return new ResponseEntity<Ad>(HttpStatus.OK);
        } else { // ищем объявление среди объявлений пользователя
            Ads ads = adsService.getMyAds();
            if (!ads.getResults.contains(oldAd)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } else {
                adsService.updateAd(id, newAd);
                return new ResponseEntity<Ad>(HttpStatus.OK);
            }
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
