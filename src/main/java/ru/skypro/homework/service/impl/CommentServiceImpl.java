package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private CommentRepository commentRepository;
    @Override
    public List<Comment> getCommentsById(Integer id) {
        return commentRepository.findById(id).stream().collect(Collectors.toList());
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        comment.setPk(adId);
        comment.setText(createOrUpdateComment.getText());
        return commentRepository.save(comment);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        Comment comment = commentRepository.findByAdIdAndId(adId, commentId);
        if (comment != null) {
            commentRepository.delete(comment);
        } else {
            try {
                throw new FileNotFoundException("Comment not found");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = commentRepository.findByAdIdAndId(adId, commentId);
        if (comment == null) {
            try {
                throw new FileNotFoundException("Comment not found");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        comment.setText(createOrUpdateComment.getText());
        return commentRepository.save(comment);
    }

}
