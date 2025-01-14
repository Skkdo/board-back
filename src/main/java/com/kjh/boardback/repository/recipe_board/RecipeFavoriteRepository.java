package com.kjh.boardback.repository.recipe_board;

import com.kjh.boardback.entity.compositeKey.RecipeFavoritePk;
import com.kjh.boardback.entity.recipe_board.RecipeFavoriteEntity;
import com.kjh.boardback.repository.resultSet.GetRecipeFavoriteListResultSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeFavoriteRepository extends JpaRepository<RecipeFavoriteEntity, RecipeFavoritePk> {

    RecipeFavoriteEntity findByBoardNumberAndUserEmail(Integer boarNumber, String UserEmail);

    @Query(
            value =
                    "SELECT " +
                            "U.email AS email, " +
                            "U.nickname AS nickname, " +
                            "U.profile_image AS profileImage " +
                            "FROM recipe_favorite AS F " +
                            "INNER JOIN user AS U " +
                            "ON F.user_email = U.email " +
                            "WHERE F.board_number = ? ",
            nativeQuery = true
    )
    List<GetRecipeFavoriteListResultSet> getFavoriteList(Integer boardNumber);

    @Transactional
    void deleteByBoardNumber(Integer boardNumber);
}
