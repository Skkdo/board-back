package com.kjh.boardback.domain.board.repository;

import com.kjh.boardback.domain.board.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByBoard_BoardNumber(Integer boardNumber);

    void deleteByBoard_BoardNumber(Integer boardNumber);

}
