package com.kjh.boardback.domain.board.repository;

import com.kjh.boardback.domain.board.entity.Favorite;
import com.kjh.boardback.domain.board.entity.FavoritePk;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoritePk> {

  @Query("SELECT f FROM Favorite f "+
          "JOIN FETCH f.user "+
          "WHERE f.board.boardNumber = :boardNumber")
  List<Favorite> getFavoriteList(@Param("boardNumber") Integer boardNumber);

  void deleteByBoard_BoardNumber(Integer boardNumber);

  Optional<Favorite> findByBoard_BoardNumberAndUser_Email(Integer boardNumber, String email);
}
