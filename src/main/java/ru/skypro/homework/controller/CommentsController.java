package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;


/**
 * REST-контроллер для управления комментариями к объявлениям.
 * Предоставляет эндпоинты для выполнения операций с комментариями, таких как:
 * <ul>
 *     <li>Получение списка комментариев для объявления</li>
 *     <li>Добавление нового комментария к объявлению</li>
 *     <li>Удаление комментария</li>
 *     <li>Обновление комментария</li>
 * </ul>
 * Операции, связанные с изменением данных, доступны только авторизованным пользователям с ролью {@link Role#USER} или {@link Role#ADMIN}.
 */
@RestController
@RequestMapping("/ads")
public class CommentsController {

    private final CommentService commentService;

    /**
     * Конструктор для создания экземпляра CommentsController.
     *
     * @param commentService Сервис для работы с комментариями.
     */
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Получает список комментариев для указанного объявления.
     *
     * @param adId Идентификатор объявления.
     * @return {@link ResponseEntity} со списком объектов {@link Comment}.
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("adId") Integer adId) {
        List<Comment> comments = commentService.getCommentsById(adId).getResults();
        return ResponseEntity.ok(comments);
    }

    /**
     * Добавляет новый комментарий к указанному объявлению.
     *
     * @param adId         Идентификатор объявления.
     * @param createComment Объект {@link CreateOrUpdateComment}, содержащий данные для создания комментария.
     * @return {@link ResponseEntity} с созданным объектом {@link Comment}.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("adId") Integer adId, @RequestBody CreateOrUpdateComment createComment) {
        Comment newComment = commentService.addComment(adId, createComment);
        return ResponseEntity.ok(newComment);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     * Доступно только для пользователей с ролью {@link Role#USER} или {@link Role#ADMIN}.
     *
     * @param adId      Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @return {@link ResponseEntity} с пустым телом и статусом {@link HttpStatus#OK}.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновляет комментарий по его идентификатору.
     * Доступно только для пользователей с ролью {@link Role#USER} или {@link Role#ADMIN}.
     *
     * @param adId         Идентификатор объявления.
     * @param commentId    Идентификатор комментария.
     * @param updateRequest Объект {@link CreateOrUpdateComment}, содержащий новые данные для комментария.
     * @return {@link ResponseEntity} с обновленным объектом {@link Comment}.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") int adId,
                                                 @PathVariable("commentId") int commentId,
                                                 @RequestBody CreateOrUpdateComment updateRequest) {
        Comment updateComment = commentService.updateComment(adId, commentId, updateRequest);
        return ResponseEntity.ok(updateComment);
    }
}
