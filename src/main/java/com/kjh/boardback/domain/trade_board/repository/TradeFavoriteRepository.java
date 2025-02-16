package com.kjh.boardback.domain.trade_board.repository;

import com.kjh.boardback.domain.trade_board.entity.TradeFavoritePk;
import com.kjh.boardback.domain.trade_board.entity.TradeFavorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeFavoriteRepository extends JpaRepository<TradeFavorite, TradeFavoritePk> {

    @Query("SELECT f FROM TradeFavorite f "+
            "JOIN FETCH f.user "+
            "WHERE f.board.boardNumber = :boardNumber ")
    List<TradeFavorite> getFavoriteList(@Param("boardNumber") Integer boardNumber);


    Optional<TradeFavorite> findByBoard_BoardNumberAndUser_Email(Integer boardNumber, String email);

    void deleteByBoard_BoardNumber(Integer boardNumber);
}
