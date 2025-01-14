package com.kjh.boardback.repository.board;

import com.kjh.boardback.repository.resultSet.GetFavoriteListResultSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kjh.boardback.entity.board.Favorite;
import com.kjh.boardback.entity.primaryKey.FavoritePk;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoritePk> {

    Favorite findByBoardNumberAndUserEmail(Integer boarNumber, String UserEmail);

    @Query(
            value =
            "SELECT "+
                    "U.email AS email, "+
                    "U.nickname AS nickname, "+
                    "U.profile_image AS profileImage "+
                    "FROM favorite AS F "+
                    "INNER JOIN user AS U "+
                    "ON F.user_email = U.email "+
                    "WHERE F.board_number = ? ",
            nativeQuery = true
    )
    List<GetFavoriteListResultSet> getFavoriteList(Integer boardNumber);

    @Transactional
    void deleteByBoardNumber(Integer boardNumber);
}
