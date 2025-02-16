package com.kjh.boardback.domain.recipe_board.repository;

import com.kjh.boardback.domain.recipe_board.entity.RecipeFavoritePk;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeFavoriteRepository extends JpaRepository<RecipeFavorite, RecipeFavoritePk> {

    Optional<RecipeFavorite> findByBoard_BoardNumberAndUser_Email(Integer boarNumber, String UserEmail);

    @Query("SELECT f FROM RecipeFavorite f " +
            "JOIN FETCH f.user " +
            "WHERE f.board.boardNumber = :boardNumber")
    List<RecipeFavorite> getFavoriteListWithUser(@Param("boardNumber") Integer boardNumber);

    void deleteByBoard_BoardNumber(Integer boardNumber);
}
