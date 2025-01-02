package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapperImpl;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapperImpl commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    void testGetCommentsById_Success() {
        // Arrange
        int adId = 1;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = List.of(comment1, comment2);
        Comments commentsDto = new Comments();
        commentsDto.setCount(comments.size());
        commentsDto.setResults(comments);

        when(commentRepository.findByAdPk(adId)).thenReturn(comments);
        when(commentMapper.mapToDto(comments)).thenReturn(commentsDto);

        // Act
        Comments result = commentService.getCommentsById(adId);

        // Assert
        assertEquals(commentsDto, result);
        verify(commentRepository).findByAdPk(adId);
        verify(commentMapper).mapToDto(comments);
    }

    @Test
    void testAddComment_Success() {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        Comment comment = new Comment();

        when(commentMapper.mapInComment(adId, createOrUpdateComment)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);

        // Act
        Comment result = commentService.addComment(adId, createOrUpdateComment);

        // Assert
        assertEquals(comment, result);
        verify(commentMapper).mapInComment(adId, createOrUpdateComment);
        verify(commentRepository).save(comment);
    }

    @Test
    void testDeleteComment_Success() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;
        Comment comment = new Comment();

        when(commentRepository.findByAdIdAndId(adId, commentId)).thenReturn(comment);

        // Act
        commentService.deleteComment(adId, commentId);

        // Assert
        verify(commentRepository).findByAdIdAndId(adId, commentId);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testDeleteComment_NotFound() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;

        when(commentRepository.findByAdIdAndId(adId, commentId)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> commentService.deleteComment(adId, commentId));
        verify(commentRepository).findByAdIdAndId(adId, commentId);
        verify(commentRepository, never()).delete(any());
    }

    @Test
    void testUpdateComment_Success() {
        // Arrange
        Integer adId = 1;
        Integer commentId = 1;
        CreateOrUpdateComment createOrUpdateComment = new CreateOrUpdateComment();
        Comment existingComment = new Comment();
        Comment updatedComment = new Comment();

        when(commentRepository.findByAdIdAndId(adId, commentId)).thenReturn(existingComment);
        when(commentMapper.mapToCreateOrUpdateComment(existingComment, createOrUpdateComment)).thenReturn(updatedComment);
        when(commentRepository.save(updatedComment)).thenReturn(updatedComment);

        // Act
        Comment result = commentService.updateComment(adId, commentId, createOrUpdateComment);

        // Assert
        assertEquals(updatedComment, result);
        verify(commentRepository).findByAdIdAndId(adId, commentId);
        verify(commentMapper).mapToCreateOrUpdateComment(existingComment, createOrUpdateComment);
        verify(commentRepository).save(updatedComment);
    }
}