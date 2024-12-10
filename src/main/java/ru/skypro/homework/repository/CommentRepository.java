package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comments getAllAdComments(long id);
}
