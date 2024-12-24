package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;


@RestController
@RequestMapping("/ads")

public class CommentsController {

    private final CommentService commentService;

    public CommentsController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}/comments")  // получение комментариев объявления
    public ResponseEntity <List<Comment>> getComments(@PathVariable("adId") Integer adId) {
        List<Comment> comments = commentService.getCommentsById(adId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")  // добавление комментариев к объявлению
    public ResponseEntity<Comment> addComment(@PathVariable("adId") Integer adId, @RequestBody CreateOrUpdateComment createComment) {
        Comment newComment = commentService.addComment(adId, createComment);
        return ResponseEntity.ok(newComment);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("{adId}/comments/{commentId}") // удаление комментария
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("{adId}/comments/{commentId}") // обновление комментария
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") int adId,
                                              @PathVariable("commentId") int commentId,
                                              @RequestBody CreateOrUpdateComment updateRequest) {
        Comment updateComment = commentService.updateComment(adId, commentId, updateRequest);
        return ResponseEntity.ok(updateComment);


    }


}
