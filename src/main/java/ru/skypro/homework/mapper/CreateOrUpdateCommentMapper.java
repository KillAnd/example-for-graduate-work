package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

@Mapper
public interface CreateOrUpdateCommentMapper {

    CreateOrUpdateComment mapToreateOrUpdateComment(Comment comment);
    Comment mapFromCreateOrUpdateComment(CreateOrUpdateComment dto);
}
