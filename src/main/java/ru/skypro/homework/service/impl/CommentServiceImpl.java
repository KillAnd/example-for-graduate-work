package ru.skypro.homework.service.impl;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentMapper;
import ru.skypro.homework.service.CommentService;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentMapper commentMapper, CommentRepository commentRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override  // изменил Integer на int чтобы не обрабатывать null(если что исправлю) так же изменил метод сервиса уже под маппер
    public Comments getCommentsById(int id) {
        return commentMapper.mapToDto(commentRepository.findById(id));
    }

    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        return commentRepository.save(commentMapper.mapInComment(adId, createOrUpdateComment)) ;

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
        Comment comment = commentMapper.mapToCreateOrUpdateComment((commentRepository.findByAdIdAndId(adId, commentId)), createOrUpdateComment);
        return commentRepository.save(comment);

    }


}
