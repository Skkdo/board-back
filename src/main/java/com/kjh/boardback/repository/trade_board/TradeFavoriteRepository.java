package com.kjh.boardback.repository.trade_board;

import com.kjh.boardback.entity.TradeFavoriteEntity;
import com.kjh.boardback.entity.primaryKey.FavoritePk;
import com.kjh.boardback.repository.resultSet.GetTradeFavoriteListResultSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeFavoriteRepository extends JpaRepository<TradeFavoriteEntity, FavoritePk> {

    TradeFavoriteEntity findByBoardNumberAndUserEmail(Integer boarNumber, String UserEmail);

    @Query(
            value =
                    "SELECT " +
                            "U.email AS email, " +
                            "U.nickname AS nickname, " +
                            "U.profile_image AS profileImage " +
                            "FROM trade_favorite AS F " +
                            "INNER JOIN user AS U " +
                            "ON F.user_email = U.email " +
                            "WHERE F.board_number = ? ",
            nativeQuery = true
    )
    List<GetTradeFavoriteListResultSet> getFavoriteList(Integer boardNumber);

    @Transactional
    void deleteByBoardNumber(Integer boardNumber);
}
