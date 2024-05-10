package com.kjh.boardback.repository.trade_board;

import com.kjh.boardback.entity.TradeBoardListViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TradeBoardListViewRepository extends JpaRepository<TradeBoardListViewEntity, Integer> {


    List<TradeBoardListViewEntity> findByOrderByWriteDatetimeDesc();


    // List<TradeBoardListViewEntity> findTop3ByWriteDatetimeGreaterThanOrderByFavoriteCountDescCommentCountDescViewCountDescWriteDatetimeDesc(String writeDateTime);
    @Query(value = "SELECT * FROM trade_board_list_view " +
            "WHERE write_datetime > ?1 " +
            "ORDER BY favorite_count DESC, comment_count DESC, " +
            "view_count DESC, write_datetime DESC " +
            "LIMIT 3", nativeQuery = true)
    List<TradeBoardListViewEntity> getTop3BoardList(@Param("writeDateTime") String writeDateTime);

    // List<TradeBoardListViewEntity> findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(String title,String content);
    @Query(value =
            "SELECT * FROM trade_board_list_view " +
                    "WHERE title LIKE %?1% OR content LIKE %?2% " +
                    "ORDER BY write_datetime DESC ",
            nativeQuery = true
    )
    List<TradeBoardListViewEntity> getSearchBoardList(String title, String content);

    List<TradeBoardListViewEntity> findByWriterEmailOrderByWriteDatetimeDesc(String writerEmail);

}
