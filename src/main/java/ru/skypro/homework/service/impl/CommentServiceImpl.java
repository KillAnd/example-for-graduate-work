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


/**
 * Реализация сервиса для работы с комментариями.
 * Предоставляет методы для получения, добавления, обновления и удаления комментариев.
 */
@Service
public class CommentServiceImpl implements CommentService {

    Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentMapperImpl commentMapper;
    private final CommentRepository commentRepository;

    /**
     * Конструктор для инициализации сервиса.
     *
     * @param commentMapper Маппер для преобразования сущностей комментариев.
     * @param commentRepository Репозиторий для работы с комментариями.
     */
    public CommentServiceImpl(CommentMapperImpl commentMapper, CommentRepository commentRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    /**
     * Получение всех комментариев для указанного объявления.
     *
     * @param id Идентификатор объявления.
     * @return Объект Comments, содержащий список комментариев.
     */
    @Override
    public Comments getCommentsById(int id) {
        List<Comment> comments = commentRepository.findByAdPk(id);
        logger.info("Получена сущность комментария: {}", comments);
        return commentMapper.mapToDto(comments);
    }

    /**
     * Добавление нового комментария к указанному объявлению.
     *
     * @param adId Идентификатор объявления.
     * @param createOrUpdateComment Данные для создания комментария.
     * @return Созданный комментарий.
     */
    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        return commentRepository.save(commentMapper.mapInComment(adId, createOrUpdateComment));
    }

    /**
     * Удаление комментария по его идентификатору.
     *
     * @param adId Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @throws FileNotFoundException если комментарий не найден.
     */
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

    /**
     * Обновление комментария по его идентификатору.
     *
     * @param adId Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @param createOrUpdateComment Новые данные для обновления комментария.
     * @return Обновленный комментарий.
     */
    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = commentMapper.mapToCreateOrUpdateComment((commentRepository.findByAdIdAndId(adId, commentId)), createOrUpdateComment);
        return commentRepository.save(comment);
    }
}