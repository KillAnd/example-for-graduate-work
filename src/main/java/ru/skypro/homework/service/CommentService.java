package ru.skypro.homework.service;

import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.List;

public interface CommentService {
    public List<Comment> getCommentsById(Integer id);

    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment);

    public void deleteComment(Integer adId, Integer commentId);

    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment);

}
