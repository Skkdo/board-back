package com.kjh.boardback.repository.board;

import com.kjh.boardback.entity.board.Board;
import java.util.List;
import java.util.Optional;
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
    List<Board> findBySearchWord(@Param("title") String title, @Param("content") String content);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.writer " +
            "WHERE b.createdAt >= CURRENT_DATE - 7 " +
            "ORDER BY b.viewCount DESC, b.favoriteCount DESC")
    List<Board> findTop3Within7Days(Pageable pageable);

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.writer " +
            "ORDER BY b.createdAt DESC")
    List<Board> getLatestBoardList();

    @Query("SELECT b FROM Board b " +
            "JOIN FETCH b.writer " +
            "WHERE b.boardNumber = :boardNumber")
    Optional<Board> getBoardWithWriter(@Param("boardNumber") Integer boardNumber);

}
