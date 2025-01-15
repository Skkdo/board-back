package com.kjh.boardback.repository.trade_board;

import com.kjh.boardback.entity.trade_board.TradeImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeImageRepository extends JpaRepository<TradeImage, Integer> {

    void deleteByBoard_BoardNumber(Integer boardNumber);

    List<TradeImage> findByBoard_BoardNumber(Integer boardNumber);
}
