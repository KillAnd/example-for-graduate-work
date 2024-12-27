package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.mapper.CommentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс, реализующий интерфейс {@link CommentMapper}.
 * Предоставляет методы для преобразования объектов между DTO и сущностями, связанными с комментариями.
 */
@Component
public class CommentMapperImpl implements CommentMapper {

    /**
     * Преобразует список сущностей {@link Comment} в объект {@link Comments}.
     *
     * @param comments Список сущностей {@link Comment}.
     * @return Объект {@link Comments}, содержащий количество комментариев и их список.
     */
    public Comments mapToDto(List<Comment> comments) {
        Comments dto = new Comments();
        dto.setCount(comments.size());
        dto.setResults(List.of(comments.toArray(new Comment[0])));
        return dto;
    }

    /**
     * Преобразует объект {@link Comments} в список сущностей {@link Comment}.
     *
     * @param comments Объект {@link Comments}.
     * @return Список сущностей {@link Comment}.
     */
    public List<Comment> mapFromDto(Comments comments) {
        return new ArrayList<>((comments.getResults()));
    }

    /**
     * Обновляет сущность {@link Comment} данными из объекта {@link CreateOrUpdateComment}.
     *
     * @param comment               Сущность {@link Comment}, которую необходимо обновить.
     * @param createOrUpdateComment Объект {@link CreateOrUpdateComment}, содержащий новые данные.
     * @return Обновлённая сущность {@link Comment}.
     */
    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment) {
        if (createOrUpdateComment != null) {
            comment.setText(createOrUpdateComment.getTextDTO());
        }
        return comment;
    }

    /**
     * Создает новую сущность {@link Comment} на основе данных из объекта {@link CreateOrUpdateComment}.
     *
     * @param adId                  Идентификатор объявления, к которому относится комментарий.
     * @param createOrUpdateComment Объект {@link CreateOrUpdateComment}, содержащий данные для создания комментария.
     * @return Новая сущность {@link Comment}.
     */
    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        if (createOrUpdateComment != null) {
            comment.setAuthor(adId);
            comment.setText(createOrUpdateComment.getTextDTO());
        }
        return comment;
    }
}