package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.Instant;
import java.util.List;

/**
 * Реализация маппера для преобразования между сущностью Comment и её DTO представлениями.
 * Этот класс предоставляет методы для преобразования объектов CreateOrUpdateComment, CommentDTO и Comments
 * в сущность Comment и наоборот.
 */
@Component
public class CommentMapperImpl {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    /**
     * Конструктор для инициализации маппера.
     *
     * @param userRepository репозиторий для работы с пользователями
     * @param adRepository   репозиторий для работы с объявлениями
     */
    public CommentMapperImpl(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    /**
     * Преобразует объект CreateOrUpdateComment в сущность Comment.
     *
     * @param createOrUpdateComment объект CreateOrUpdateComment, содержащий данные для создания или обновления комментария
     * @param username              имя пользователя, оставившего комментарий
     * @param id                    идентификатор объявления, к которому относится комментарий
     * @return сущность Comment
     * @throws NullPointerException если переданный объект createOrUpdateComment равен null
     */
    public Comment toCommentEntity(CreateOrUpdateComment createOrUpdateComment, String username, int id) {
        if (createOrUpdateComment == null) {
            throw new NullPointerException("Переданный объект comment is null");
        }

        User userEntity = userRepository.findByUsername(username);
        Ad adEntity = adRepository.findAdByPk(id);

        Comment commentEntity = new Comment();
        commentEntity.setAuthor(userEntity.getId());
        commentEntity.setAuthorImage(userEntity.getImage());
        commentEntity.setAuthorFirstName(userEntity.getFirstName());
        commentEntity.setCreatedAt(Instant.now().toEpochMilli());
        commentEntity.setText(createOrUpdateComment.getTextComment());
        commentEntity.setUserCom(userEntity);
        commentEntity.setAd(adEntity);
        return commentEntity;
    }

    /**
     * Преобразует сущность Comment в объект CommentDTO.
     *
     * @param commentEntity сущность Comment
     * @return объект CommentDTO
     * @throws NullPointerException если переданный объект commentEntity равен null
     */
    public CommentDTO toCommentDto(Comment commentEntity) {
        if (commentEntity == null) {
            throw new NullPointerException("Переданный объект commentEntity is null");
        }
        CommentDTO comment = new CommentDTO();
        comment.setAuthor(commentEntity.getAuthor());
        comment.setAuthorImage(commentEntity.getAuthorImage());
        comment.setAuthorFirstName(commentEntity.getAuthorFirstName());
        comment.setCreatedAt(commentEntity.getCreatedAt());
        comment.setPk(commentEntity.getPk());
        comment.setText(commentEntity.getText());
        return comment;
    }

    /**
     * Преобразует список сущностей Comment в объект Comments.
     *
     * @param comments список сущностей Comment
     * @return объект Comments
     */
    public Comments mapToDto(List<Comment> comments) {
        Comments dto = new Comments();
        dto.setCount(comments.size());
        dto.setResults(comments);
        return dto;
    }

    /**
     * Преобразует объект Comments в список сущностей Comment.
     *
     * @param comments объект Comments
     * @return список сущностей Comment
     */
    public List<Comment> mapFromDto(Comments comments) {
        return comments.getResults();
    }

    /**
     * Обновляет сущность Comment на основе объекта CreateOrUpdateComment.
     *
     * @param comment                сущность Comment для обновления
     * @param createOrUpdateComment объект CreateOrUpdateComment, содержащий новые данные
     * @return обновленная сущность Comment
     */
    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment) {
        if (createOrUpdateComment != null) {
            comment.setText(createOrUpdateComment.getTextComment());
        }
        return comment;
    }

    /**
     * Создает сущность Comment на основе объекта CreateOrUpdateComment и идентификатора объявления.
     *
     * @param adId                   идентификатор объявления
     * @param createOrUpdateComment объект CreateOrUpdateComment, содержащий данные для создания комментария
     * @return сущность Comment
     */
    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        if (createOrUpdateComment != null) {
            comment.setAuthor(adId);
            comment.setText(createOrUpdateComment.getTextComment());
        }
        return comment;
    }
}