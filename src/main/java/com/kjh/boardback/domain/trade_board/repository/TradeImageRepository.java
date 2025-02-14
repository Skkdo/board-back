package com.kjh.boardback.domain.trade_board.repository;

import com.kjh.boardback.domain.trade_board.entity.TradeImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeImageRepository extends JpaRepository<TradeImage, Integer> {

    void deleteByBoard_BoardNumber(Integer boardNumber);

    List<TradeImage> findByBoard_BoardNumber(Integer boardNumber);
}
