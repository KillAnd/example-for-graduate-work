
package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.model.Comment;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Comment}.
 * Предоставляет методы для выполнения операций с комментариями в базе данных.
 * Расширяет {@link JpaRepository}, что позволяет использовать стандартные методы JPA.
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    /**
     * Находит список комментариев по идентификатору.
     *
     * @param id Идентификатор комментария.
     * @return Список комментариев с указанным идентификатором.
     */
    List<Comment> findById(int id);

    /**
     * Находит комментарий по идентификатору объявления и идентификатору комментария.
     * Использует пользовательский JPQL-запрос для поиска.
     *
     * @param adId      Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @return Найденный комментарий или {@code null}, если комментарий не найден.
     */
    @Query("SELECT c FROM Comment c WHERE c.ad.id = :adId AND c.id = :commentId")
    Comment findByAdIdAndId(@Param("adId") Integer adId, @Param("commentId") Integer commentId);
}



