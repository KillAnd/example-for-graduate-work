package ru.skypro.homework.service;

import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

public interface CommentService {


    Comments getCommentsById(int id);

    Comment addComment(Integer adId, CreateOrUpdateComment createOrUpdateComment);

    void deleteComment(Integer adId, Integer commentId);

    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment createOrUpdateComment);

}
