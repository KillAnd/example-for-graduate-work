package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.dto.Ads;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Ad}.
 * Предоставляет методы для выполнения операций с объявлениями в базе данных.
 * Расширяет {@link JpaRepository}, что позволяет использовать стандартные методы JPA.
 */
public interface AdRepository extends JpaRepository<Ad, Integer> {

    /**
     * Находит все объявления, созданные пользователем с указанным идентификатором.
     *
     * @param currentUserId Идентификатор пользователя (автора объявлений).
     * @return Список объявлений, созданных пользователем.
     */
    List<Ad> findAdsByAuthor(int currentUserId);
}
