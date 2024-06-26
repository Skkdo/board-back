package com.kjh.boardback.repository.board;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kjh.boardback.entity.board.ImageEntity;

import java.util.List;

@Repository

public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{

    List<ImageEntity> findByBoardNumber(Integer boardNumber);

    @Transactional
    void deleteByBoardNumber(Integer boardNumber);
    
}
