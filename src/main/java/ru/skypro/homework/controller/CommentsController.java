package ru.skypro.homework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
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

    @GetMapping("/{id}/comments")
    public ResponseEntity <List<Comment>> getComments(@PathVariable("id") Integer id) {
        List<Comment> comments = commentService.getCommentsByAdId(id);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable("id") Integer id, @RequestBody CreateOrUpdateComment createComment) {
        Comment newComment = commentService.addComment(id, createComment);
        return ResponseEntity.ok(newComment);
    }

    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();

    }
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable("adId") int adId,
                                              @PathVariable("commentId") int commentId,
                                              @RequestBody CreateOrUpdateComment updateRequest) {
        Comment updateComment = commentService.updateComment(adId, commentId, updateRequest);
        return ResponseEntity.ok(updateComment);


    }


}
