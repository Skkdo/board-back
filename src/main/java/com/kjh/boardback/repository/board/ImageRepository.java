package com.kjh.boardback.repository.board;

import com.kjh.boardback.entity.board.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    List<Image> findByBoard_BoardNumber(Integer boardNumber);

    void deleteByBoard_BoardNumber(Integer boardNumber);

}
