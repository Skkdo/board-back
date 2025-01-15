package com.kjh.boardback.repository.recipe_board;

import com.kjh.boardback.entity.recipe_board.RecipeComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Integer> {

    @Query("SELECT c FROM RecipeComment c " +
            "JOIN FETCH c.writer " +
            "WHERE c.board.boardNumber = :boardNumber "+
            "ORDER BY c.createdAt ASC ")
    List<RecipeComment> getCommentListWithUser(@Param("boardNumber") Integer boardNumber);

    Optional<RecipeComment> findByCommentNumber(Integer commentNumber);

    void deleteByBoard_BoardNumber(Integer boardNumber);
}
