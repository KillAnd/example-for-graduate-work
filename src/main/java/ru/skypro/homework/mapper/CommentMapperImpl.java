package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

import java.util.Arrays;
import java.util.List;

@Component
public class CommentMapperImpl implements CommentsMapper, CreateOrUpdateCommentMapper{

    @Override
    public Comments mapToComments(List<Comment> comments) {
        Comments dto = new Comments();
        dto.setCount(comments.size());
        dto.setResults(comments);
        return dto;
    }

    @Override
    public List<Comment> mapFromComments(Comments dto) {
        return dto.getResults();
    }

    @Override
    public CreateOrUpdateComment mapToreateOrUpdateComment(Comment comment) {
        CreateOrUpdateComment dto = new CreateOrUpdateComment();
        dto.setText(comment.getText());
        return dto;
    }

    @Override
    public Comment mapFromCreateOrUpdateComment(CreateOrUpdateComment dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        return comment;
    }
}
