package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.service.CommentService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentsControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentsController commentsController;

    @Test
    void testGetComments_Success() {
        // Arrange
        Integer adId = 1;
        Comments comments = new Comments();
        comments.setResults(Collections.emptyList());
        comments.setCount(0);

        when(commentService.getCommentsById(adId)).thenReturn(comments);

        // Act
        ResponseEntity<Comments> response = commentsController.getComments(adId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(comments, response.getBody());
        verify(commentService).getCommentsById(adId);
    }

    @Test
    void testAddComment_Success() {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateComment createComment = new CreateOrUpdateComment();
        createComment.setText("New comment");

        Comment newComment = new Comment();
        newComment.setText("New comment");

        when(commentService.addComment(adId, createComment)).thenReturn(newComment);

        // Act
        ResponseEntity<Comment> response = commentsController.addComment(adId, createComment);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(newComment, response.getBody());
        verify(commentService).addComment(adId, createComment);
    }

    @Test
    void testDeleteComment_Success() {
        // Arrange
        int adId = 1;
        Integer commentId = 1;

        doNothing().when(commentService).deleteComment(adId, commentId);

        // Act
        ResponseEntity<Void> response = commentsController.deleteComment(adId, commentId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(commentService).deleteComment(adId, commentId);
    }

    @Test
    void testUpdateComment_Success() {
        // Arrange
        int adId = 1;
        int commentId = 1;
        CreateOrUpdateComment updateRequest = new CreateOrUpdateComment();
        updateRequest.setText("Updated comment");

        Comment updatedComment = new Comment();
        updatedComment.setText("Updated comment");

        when(commentService.updateComment(adId, commentId, updateRequest)).thenReturn(updatedComment);

        // Act
        ResponseEntity<Comment> response = commentsController.updateComment(adId, commentId, updateRequest);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedComment, response.getBody());
        verify(commentService).updateComment(adId, commentId, updateRequest);
    }
}