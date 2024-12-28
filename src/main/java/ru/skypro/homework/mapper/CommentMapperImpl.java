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
import java.util.stream.Collectors;

@Component
public class CommentMapperImpl {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public CommentMapperImpl(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    //из дто в сущность
    public Comment toCommentEntity(CreateOrUpdateComment createOrUpdateComment,
                                         String username,
                                         int id) {
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

    //из сущности в дто Comment
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



    public Comments mapToDto(List<Comment> comments) {
        Comments dto = new Comments();
        dto.setCount(comments.size());
        dto.setResults(comments);
        return dto;
    }

    public List<Comment> mapFromDto(Comments comments) {
        return comments.getResults();
    }

    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment) {
        if (createOrUpdateComment != null) {


            comment.setText(createOrUpdateComment.getTextComment());


        }
        return comment;
    }

    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        if (createOrUpdateComment != null) {
            comment.setAuthor(adId);
            comment.setText(createOrUpdateComment.getTextComment());
        }
        return comment;
    }
}