package com.kjh.boardback.repository.trade_board;

import com.kjh.boardback.entity.TradeImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface TradeImageRepository extends JpaRepository<TradeImageEntity, Integer> {

    List<TradeImageEntity> findByBoardNumber(Integer boardNumber);

    @Transactional
    void deleteByBoardNumber(Integer boardNumber);

}
