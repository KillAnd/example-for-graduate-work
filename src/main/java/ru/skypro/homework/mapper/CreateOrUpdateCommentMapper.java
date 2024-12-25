package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

public interface CreateOrUpdateCommentMapper {

    CreateOrUpdateComment mapToCreateOrUpdateComment(Comment comment);
    Comment mapFromCreateOrUpdateComment(CreateOrUpdateComment dto);
}
