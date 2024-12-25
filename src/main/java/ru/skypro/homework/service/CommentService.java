package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;

import java.util.List;

public interface CommentService {


    // изменил Integer на int чтобы не обрабатывать null(если что исправлю) так же изменил метод сервиса уже под маппер
    Comments getCommentsById(int id);

    public Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment);

    public void deleteComment(Integer adId, Integer commentId);

    public Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment);

}
