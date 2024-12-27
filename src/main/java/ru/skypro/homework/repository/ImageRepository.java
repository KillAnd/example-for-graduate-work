package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Image}.
 * Предоставляет методы для выполнения операций с изображениями в базе данных.
 * Расширяет {@link JpaRepository}, что позволяет использовать стандартные методы JPA.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {

    /**
     * Находит изображение, связанное с указанным пользователем.
     *
     * @param user Пользователь, связанный с изображением.
     * @return {@link Optional}, содержащий изображение, если оно найдено, иначе пустой {@link Optional}.
     */
    Optional<Image> findByUser(User user);

    /**
     * Находит изображение, связанное с указанным объявлением.
     *
     * @param ad Объявление, связанное с изображением.
     * @return {@link Optional}, содержащий изображение, если оно найдено, иначе пустой {@link Optional}.
     */
    Optional<Image> findByAd(Ad ad);

    /**
     * Находит изображение по его уникальному идентификатору.
     *
     * @param id Идентификатор изображения.
     * @return {@link Optional}, содержащий изображение, если оно найдено, иначе пустой {@link Optional}.
     */
    Optional<Image> findById(Long id);
}
