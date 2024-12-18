package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper
public interface CommentsMapper {

    Comments mapToComments(List<Comment> comments);
    List<Comment> mapFromComments(Comments comments);
}
