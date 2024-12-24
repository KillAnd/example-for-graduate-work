package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Mapper
public interface CommentsMapper {

    default Comments mapToComments(List<Comment> comments) {
        Comments result = new Comments();
        result.setResults(comments);
        return result;
    }

    default List<Comment> mapFromComments(Comments comments) {
        return comments.getResults();
    }
}
