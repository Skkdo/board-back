package com.kjh.boardback.repository.board;

import com.kjh.boardback.entity.board.Comment;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("SELECT c FROM Comment c "+
            "JOIN FETCH c.writer "+
            "WHERE c.board.boardNumber = :boardNumber "+
            "ORDER BY c.createdAt DESC")
    List<Comment> getCommentList(@Param("boardNumber") Integer boardNumber);

    Optional<Comment> findByCommentNumber(Integer commentNUmber);

    void deleteByBoard_BoardNumber(Integer boardNumber);
}
