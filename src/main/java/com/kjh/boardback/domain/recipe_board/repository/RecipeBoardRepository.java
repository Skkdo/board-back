package com.kjh.boardback.domain.recipe_board.repository;

import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeBoardRepository extends JpaRepository<RecipeBoard, Integer> {

    Optional<RecipeBoard> findByBoardNumber(Integer boardNumber);

    @Query("SELECT b FROM RecipeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE b.type = :type " +
            "ORDER BY b.createdAt ASC ")
    List<RecipeBoard> getLatestList(@Param("type") int type);

    @Query("SELECT b FROM RecipeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE (b.type = :type AND b.createdAt >= :sevenDaysAgo) " +
            "ORDER BY b.viewCount DESC, b.favoriteCount DESC")
    List<RecipeBoard> getTop3ListWithin7Days(@Param("type") int type, @Param("sevenDaysAgo") LocalDateTime sevenDaysAgo,
                                             Pageable pageable);

    @Query("SELECT b FROM RecipeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE (b.title LIKE %:title% AND b.content LIKE %:content%) " +
            "ORDER BY b.createdAt DESC ")
    List<RecipeBoard> getBySearchWord(@Param("title") String title, @Param("content") String content);

    @Query("SELECT b FROM RecipeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE b.writer.email = :email " +
            "ORDER BY b.createdAt DESC ")
    List<RecipeBoard> getUserBoardList(@Param("email") String email);
}
