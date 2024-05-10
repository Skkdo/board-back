package com.kjh.boardback.repository.trade_board;

import com.kjh.boardback.entity.TradeBoardEntity;
import com.kjh.boardback.repository.resultSet.GetTradeBoardResultSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeBoardRepository extends JpaRepository<TradeBoardEntity, Integer> {

    @Query(
            value =
                    "SELECT " +
                            "B.board_number AS boardNumber, " +
                            "B.title AS title, " +
                            "B.content AS content, " +
                            "B.write_datetime AS writeDatetime, " +
                            "B.writer_email AS writerEmail, " +
                            "B.trade_location AS tradeLocation, " +
                            "U.nickname AS writerNickname, " +
                            "U.profile_image AS writerProfileImage " +
                            "FROM trade_board AS B " +
                            "INNER JOIN `user` AS U ON B.writer_email = U.email " +
                            "WHERE board_number = ?1 ",
            nativeQuery = true
    )
    GetTradeBoardResultSet getTradeBoard(Integer boardNumber);

    TradeBoardEntity findByBoardNumber(Integer boardNumber);

    Boolean existsByBoardNumber(Integer boardNumber);
}
