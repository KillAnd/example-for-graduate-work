package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public List<Comment> getCommentsById(Integer id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        comment.setPk(adId);
        comment.setText(createOrUpdateComment.getText());
        return commentRepository.save(comment);
    }

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
