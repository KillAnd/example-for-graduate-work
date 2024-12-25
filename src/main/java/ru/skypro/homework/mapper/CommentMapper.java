package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import javax.swing.*;
import java.util.List;


public interface CommentMapper {

    default Comments mapToDto(List<Comment> comments){
        Comments result = new Comments();
        result.setResults(comments);
        return result;
    };

    default List<Comment> mapFromDto(Comments comments){
        return comments.getResults();
    };

    public Comment mapToCreateOrUpdateComment(Comment comment, CreateOrUpdateComment createOrUpdateComment);

    public Comment mapInComment(Integer adId, CreateOrUpdateComment createOrUpdateComment);
}
