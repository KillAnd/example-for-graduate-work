package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.mapper.CommentMapper;

import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.service.CommentService;
import java.io.FileNotFoundException;

/**
 * Сервис для работы с комментариями.
 * Реализует интерфейс {@link CommentService} и предоставляет методы для выполнения операций
 * с комментариями, таких как получение, добавление, удаление и обновление.
 */
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    /**
     * Конструктор для создания экземпляра CommentServiceImpl.
     *
     * @param commentMapper     Маппер для преобразования между DTO и сущностями.
     * @param commentRepository Репозиторий для работы с сущностью {@link Comment}.
     */
    public CommentServiceImpl(CommentMapper commentMapper, CommentRepository commentRepository) {
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    /**
     * Возвращает комментарии по идентификатору.
     *
     * @param id Идентификатор комментария.
     * @return Объект {@link Comments}, содержащий список комментариев.
     */
    @Override
    public Comments getCommentsById(int id) {
        return commentMapper.mapToDto(commentRepository.findById(id));
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param adId                 Идентификатор объявления.
     * @param createOrUpdateComment Объект {@link CreateOrUpdateComment}, содержащий данные для создания комментария.
     * @return Созданный комментарий.
     */
    @Override
    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        return commentRepository.save(commentMapper.mapInComment(adId, createOrUpdateComment));
    }

    /**
     * Удаляет комментарий по идентификаторам объявления и комментария.
     *
     * @param adId      Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @throws FileNotFoundException Если комментарий не найден.
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
     * Обновляет комментарий по идентификаторам объявления и комментария.
     *
     * @param adId                 Идентификатор объявления.
     * @param commentId            Идентификатор комментария.
     * @param createOrUpdateComment Объект {@link CreateOrUpdateComment}, содержащий новые данные для комментария.
     * @return Обновленный комментарий.
     */
    @Override
    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = commentMapper.mapToCreateOrUpdateComment((commentRepository.findByAdIdAndId(adId, commentId)), createOrUpdateComment);
        return commentRepository.save(comment);
    }
}
