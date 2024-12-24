package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper
public interface CommentsMapper {

    @Mapping(source = "comments", target = "results")
    Comments mapToComments(List<Comment> comments);

    @Mapping(source = "results", target = "comments")
    List<Comment> mapFromComments(Comments comments);
}
