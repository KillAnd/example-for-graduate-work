package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import java.util.List;

public interface CommentMapper {
    public Comments mapToDto(List<Comment> comments);

    public List<Comment> mapFromDto(Comments comments);

    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment);

    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment);
}
