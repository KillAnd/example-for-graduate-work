package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;

/**
 * Контроллер для управления объявлениями.
 * Предоставляет REST API для выполнения операций с объявлениями, таких как получение, добавление, обновление и удаление.
 * Все методы доступны по пути "/ads".
 */
@RestController
@RequestMapping("/ads")
public class AdsController {
    Logger logger = LoggerFactory.getLogger(AdsController.class);

    private final AdsService adsService;
    private final UserRepository userRepository;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param adsService Сервис для работы с объявлениями.
     * @param userRepository Репозиторий для работы с пользователями.
     */
    public AdsController(AdsService adsService, UserRepository userRepository) {
        this.adsService = adsService;
        this.userRepository = userRepository;
    }

    /**
     * Получение всех объявлений.
     *
     * @return ResponseEntity с списком всех объявлений и статусом OK.
     */
    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class)))})
    @GetMapping()
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAllAds());
    }

    /**
     * Добавление нового объявления.
     *
     * @param ad Данные объявления.
     * @param image Изображение объявления.
     * @return ResponseEntity с созданным объявлением и статусом CREATED, или статусом UNAUTHORIZED, если пользователь не аутентифицирован.
     * @throws IOException если произошла ошибка при создании объявления.
     */
    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @PostMapping(consumes = "multipart/form-data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
    })
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd ad,
                                    @RequestPart("image") MultipartFile image) {
        logger.info("Зашли в метод по добавлению объявления");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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

    /**
     * Получение информации об объявлении по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @param authentication Данные аутентификации пользователя.
     * @return ResponseEntity с информацией об объявлении и статусом OK, или статусом UNAUTHORIZED, если пользователь не аутентифицирован, или статусом NOT_FOUND, если объявление не найдено.
     */
    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping(path = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExtendedAd.class)),
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(mediaType = "")),
    })
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id, Authentication authentication) {
        if (authentication.getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adsService.existId(id)) {
            return ResponseEntity.ok(adsService.getAdById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Удаление объявления по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return ResponseEntity с статусом NO_CONTENT, если объявление успешно удалено, или статусом UNAUTHORIZED, если пользователь не аутентифицирован, или статусом FORBIDDEN, если пользователь не имеет прав на удаление, или статусом NOT_FOUND, если объявление не найдено.
     */
    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAd(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (adsService.existId(id)) {
            adsService.deleteAd(id);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление информации об объявлении по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @param newAd Новые данные объявления.
     * @param authentication Данные аутентификации пользователя.
     * @return ResponseEntity с обновленным объявлением и статусом OK, или статусом UNAUTHORIZED, если пользователь не аутентифицирован, или статусом FORBIDDEN, если пользователь не имеет прав на обновление, или статусом NOT_FOUND, если объявление не найдено.
     */
    @Operation(summary = "Обновление информации в объявлении", tags = {"Объявления"})
    @PatchMapping(path = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ad.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Ad> updateAd(@PathVariable int id, @RequestBody CreateOrUpdateAd newAd,
                                       Authentication authentication) {
        // Получение объявления, которое необходимо обновить
        ResponseEntity<ExtendedAd> oldAdResponse = getAd(id, authentication);

        // Проверка, существует ли объявление
        if (oldAdResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Ad updatedAd = adsService.updateAd(id, newAd);
        return ResponseEntity.ok(updatedAd);
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return ResponseEntity с списком объявлений пользователя и статусом OK, или статусом UNAUTHORIZED, если пользователь не аутентифицирован.
     */
    @Operation(summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Ads.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getUserAds() {
        return ResponseEntity.ok().body(adsService.getMyAds());
    }

    /**
     * Обновление изображения объявления по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @param image Новое изображение объявления.
     * @return ResponseEntity с статусом OK, если изображение успешно обновлено, или статусом UNAUTHORIZED, если пользователь не аутентифицирован, или статусом FORBIDDEN, если пользователь не имеет прав на обновление, или статусом NOT_FOUND, если объявление не найдено.
     * @throws IOException если произошла ошибка при обновлении изображения.
     */
    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @PatchMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/octet-stream",
                            array = @ArraySchema(schema = @Schema(type = "string", format = "byte")))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "")),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "")),
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAdImage(@PathVariable Integer id, @RequestParam("image") MultipartFile image) throws IOException {
        logger.info("Метод addAds, класса AdController. Приняты: (int) id {}. Изображение объявления{}",
                id, image.getOriginalFilename());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName() == null) {
            logger.info("юзер аутентификацию не прошел");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (adsService.existId(id)) {
            adsService.updateAdImage(id, image);
            logger.info("Получена картинка. {}", image.getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
