package com.kjh.boardback.domain.trade_board.repository;

import com.kjh.boardback.domain.trade_board.entity.TradeComment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeCommentRepository extends JpaRepository<TradeComment, Integer> {

    @Query("SELECT c FROM TradeComment c "+
            "JOIN FETCH c.writer "+
            "WHERE c.board.boardNumber = :boardNumber "+
            "ORDER BY c.createdAt ASC ")
    List<TradeComment> getCommentList(@Param("boardNumber") Integer boardNumber);

    Optional<TradeComment> findByCommentNumber(Integer commentNumber);

    void deleteByBoard_BoardNumber(Integer boardNUmber);
}
