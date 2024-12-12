package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments;
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
    public ResponseEntity <Comments> getComments(@PathVariable("id") int id) {
        Comments comments = commentService.getCommentsById(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")  // добавление комментариев к объявлению
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer id, @RequestBody CreateOrUpdateComment createComment) {
        Comment newComment = commentService.addComment(id, createComment);
        return ResponseEntity.ok(newComment);
    }

    @DeleteMapping("{adId}/comments/{commentId}") // удаление комментария
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();

    }
    @PatchMapping("{adId}/comments/{commentId}") // обновление комментария
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") int adId,
                                              @PathVariable("commentId") int commentId,
                                              @RequestBody CreateOrUpdateComment updateRequest) {
        Comment updateComment = commentService.updateComment(adId, commentId, updateRequest);
        return ResponseEntity.ok(updateComment);


    }


}
