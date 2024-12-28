
package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findById(int id);
    @Query("SELECT c FROM Comment c WHERE c.ad.id = :adId AND c.id = :commentId")
    Comment findByAdIdAndId(@Param("adId") Integer adId, @Param("commentId") Integer commentId);

}




