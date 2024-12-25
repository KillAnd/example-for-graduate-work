package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentServiceImplTest {


    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment existingComment;

    @BeforeEach
    void setUp1() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommentsById_ShouldReturnComments() {
        when(commentRepository.findById(1)).thenReturn((List<Comment>) existingComment);
        when(commentMapper.mapToDto(any())).thenReturn(new Comments());

        Comments comments = commentService.getCommentsById(1);

        assertNotNull(comments);
        verify(commentRepository).findById(1);
        verify(commentMapper).mapToDto((List<Comment>) existingComment);
    }

    @Test
    void addComment_ShouldSaveAndReturnComment() {

        existingComment = new Comment();

        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("New comment");

        when(commentMapper.mapInComment(anyInt(), any())).thenReturn(existingComment);
        when(commentRepository.save(any())).thenReturn(existingComment);

        Comment savedComment = commentService.addComment(1, createOrUpdateComment);

//        assertNotNull(savedComment);
        assertEquals(existingComment.getText(), savedComment.getText());
        verify(commentMapper).mapInComment(1, createOrUpdateComment);
        verify(commentRepository).save(existingComment);
    }

    @Test
    void deleteComment_ShouldDeleteExistingComment() {
        existingComment = new Comment();
        when(commentRepository.findByAdIdAndId(1, 1)).thenReturn(existingComment);

        assertDoesNotThrow(() -> commentService.deleteComment(1, 1));
        verify(commentRepository).delete(existingComment);
    }

    @Test
    void deleteComment_ShouldThrowException_WhenCommentNotFound() {
        when(commentRepository.findByAdIdAndId(1, 2)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentService.deleteComment(1, 2);
        });
        assertEquals("java.io.FileNotFoundException: Comment not found", exception.getMessage());
    }

    @Test
    void updateComment_ShouldUpdateAndReturnUpdatedComment() {
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        createOrUpdateComment.setText("Updated comment");

        when(commentRepository.findByAdIdAndId(1, 1)).thenReturn(existingComment);
        when(commentMapper.mapToCreateOrUpdateComment(any(), any())).thenReturn(existingComment);
        when(commentRepository.save(any())).thenReturn(existingComment);

        Comment updatedComment = commentService.updateComment(1, 1, createOrUpdateComment);


    }
}