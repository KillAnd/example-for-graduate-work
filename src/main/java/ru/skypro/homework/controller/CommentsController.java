package ru.skypro.homework.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;


@RestController
@RequestMapping("/ads")

public class CommentsController {

    private final Logger logger = LoggerFactory.getLogger(CommentsController.class);

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/comments")  // получение комментариев объявления
    public ResponseEntity <Comments> getComments(@PathVariable("id") Integer adId) {
        logger.info("полученный ID объявления{}", adId);
        Comments comments = commentService.getCommentsById(adId);
        logger.info("возвращаемый комментарий{}", comments);
        return ResponseEntity.ok(comments);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("{id}/comments")  // добавление комментариев к объявлению
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer adId,
                                              @RequestBody CreateOrUpdateComment createComment) {
        Comment newComment = commentService.addComment(adId, createComment);
        return ResponseEntity.ok(newComment);
    }

    @PreAuthorize("hasRole('ADMIN') || @commentService.isCommentAuthor(#commentId, authentication.principal.id)")
    @DeleteMapping("{adId}/comments/{commentId}") // удаление комментария
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasRole('ADMIN') || @commentService.isCommentAuthor(#commentId, authentication.principal.id)")
    @PatchMapping("{adId}/comments/{commentId}") // обновление комментария
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") int adId,
                                              @PathVariable("commentId") int commentId,
                                              @RequestBody CreateOrUpdateComment updateRequest) {
        Comment updateComment = commentService.updateComment(adId, commentId, updateRequest);
        return ResponseEntity.ok(updateComment);


    }


}
