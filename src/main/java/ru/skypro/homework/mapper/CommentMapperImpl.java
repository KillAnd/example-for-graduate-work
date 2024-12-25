package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {

    public Comments mapToDto(List<Comment> comments) {
        Comments dto = new Comments();
        dto.setCount(comments.size());
        dto.setResults(List.of(comments.toArray(new Comment[0])));
        return dto;
    }

    public List<Comment> mapFromDto(Comments comments) {
        return new ArrayList<>((comments.getResults()));
    }

    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment) {
        if (createOrUpdateComment != null) {
            comment.setText(createOrUpdateComment.getTextDTO());
        }
        return comment;
    }

    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        if (createOrUpdateComment != null) {
            comment.setAuthor(adId);
            comment.setText(createOrUpdateComment.getTextDTO());
        }
        return comment;
    }
}
