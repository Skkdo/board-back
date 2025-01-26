package com.kjh.boardback.repository.board;

import com.kjh.boardback.entity.board.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<Board> findByBoardNumber(Integer boardNumber);

    List<Board> findByWriter_EmailOrderByCreatedAtDesc(String writerEmail);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.writer " +
            "WHERE (b.title LIKE %:title% OR b.content LIKE %:content%) " +
            "ORDER BY b.createdAt DESC")
    List<Board> getBySearchWord(@Param("title") String title, @Param("content") String content);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.writer " +
            "WHERE b.createdAt >= :sevenDaysAgo " +
            "ORDER BY b.viewCount DESC, b.favoriteCount DESC")
    List<Board> getTop3Within7Days(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo, Pageable pageable);

    @Query(value = "SELECT b FROM Board b JOIN FETCH b.writer",
            countQuery = "SELECT COUNT (b) FROM Board b")
    Page<Board> getLatestBoardList(Pageable pageable);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.writer " +
            "WHERE b.boardNumber = :boardNumber")
    Optional<Board> getBoardWithWriter(@Param("boardNumber") Integer boardNumber);

}
