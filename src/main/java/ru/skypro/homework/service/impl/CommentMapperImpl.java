package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.service.CommentMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentMapper {
    public Comments mapToDto(List<Comment> comments) {
        Comments dto = new Comments();
        dto.setCount(comments.size());
        dto.setResults(comments.toArray(new Comment[0]));
        return dto;
    }

    public List<Comment> mapFromDto(Comments comments) {
        return new ArrayList<>(Arrays.asList(comments.getResults()));
    }
    // дальше сомневаюсь, оставлю пока так, надо посмтореть что дальше

    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment) {
        comment.setText(createOrUpdateComment.getText());
        return comment;
    }

    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment) {
        Comment comment = new Comment();
        comment.setAuthor(adId);
        comment.setText(createOrUpdateComment.getText());
        return comment;
    }

}
