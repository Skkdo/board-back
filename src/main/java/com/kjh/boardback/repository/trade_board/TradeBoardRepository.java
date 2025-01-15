package com.kjh.boardback.repository.trade_board;


import com.kjh.boardback.entity.trade_board.TradeBoard;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeBoardRepository extends JpaRepository<TradeBoard, Integer> {

    Optional<TradeBoard> findByBoardNumber(Integer boardNumber);

    List<TradeBoard> findByWriter_EmailOrderByCreatedAtDesc(String writerEmail);

    @Query("SELECT b FROM TradeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE (b.title LIKE %:title% OR b.content LIKE %:content%) " +
            "ORDER BY b.createdAt DESC")
    List<TradeBoard> getBySearchWord(@Param("title") String title, @Param("content") String content);

    @Query("SELECT b FROM TradeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE b.createdAt >= CURRENT_DATE - 7 " +
            "ORDER BY b.viewCount DESC, b.favoriteCount DESC")
    List<TradeBoard> getTop3Within7Days(Pageable pageable);

    @Query("SELECT b FROM TradeBoard b " +
            "JOIN FETCH b.writer " +
            "ORDER BY b.createdAt DESC")
    List<TradeBoard> getLatestBoardList();

    @Query("SELECT b FROM TradeBoard b " +
            "JOIN FETCH b.writer " +
            "WHERE b.boardNumber = :boardNumber")
    Optional<TradeBoard> getBoardWithWriter(@Param("boardNumber") Integer boardNumber);

}
