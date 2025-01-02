package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.service.CommentService;


/**
 * Контроллер для управления комментариями к объявлениям.
 * Предоставляет REST API для получения, добавления, обновления и удаления комментариев.
 */
@RestController
@RequestMapping("/ads")
public class CommentsController {

    private final Logger logger = LoggerFactory.getLogger(CommentsController.class);

    private final CommentService commentService;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param commentService Сервис для работы с комментариями.
     */
    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Получение всех комментариев для указанного объявления.
     *
     * @param adId Идентификатор объявления.
     * @return ResponseEntity с списком комментариев и статусом OK.
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable("id") Integer adId) {
        logger.info("Полученный ID объявления: {}", adId);
        Comments comments = commentService.getCommentsById(adId);
        logger.info("Возвращаемые комментарии: {}", comments);
        return ResponseEntity.ok(comments);
    }

    /**
     * Добавление нового комментария к указанному объявлению.
     *
     * @param adId Идентификатор объявления.
     * @param createComment Данные для создания комментария.
     * @return ResponseEntity с созданным комментарием и статусом OK.
     */
    @PostMapping("{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer adId,
                                              @RequestBody CreateOrUpdateComment createComment) {
        Comment newComment = commentService.addComment(adId, createComment);
        return ResponseEntity.ok(newComment);
    }

    /**
     * Удаление комментария по его идентификатору.
     * Доступно только для пользователей с ролью ROLE_USER или ROLE_ADMIN.
     *
     * @param adId Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @return ResponseEntity с статусом OK.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление комментария по его идентификатору.
     * Доступно только для пользователей с ролью ROLE_USER или ROLE_ADMIN.
     *
     * @param adId Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @param updateRequest Данные для обновления комментария.
     * @return ResponseEntity с обновленным комментарием и статусом OK.
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