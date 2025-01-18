package com.kjh.boardback.repository.board;

import com.kjh.boardback.entity.board.Favorite;
import com.kjh.boardback.entity.compositeKey.FavoritePk;
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
