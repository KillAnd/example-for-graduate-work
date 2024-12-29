package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.mapper.CommentMapperImpl;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import java.io.FileNotFoundException;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentMapperImpl commentMapper;

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentMapperImpl commentMapper, CommentRepository commentRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comments getCommentsById(int id) {
        List<Comment> comments = commentRepository.findByAdPk(id);
        logger.info("Получена сущность комментария {}", comments);
        return commentMapper.mapToDto(comments);
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
