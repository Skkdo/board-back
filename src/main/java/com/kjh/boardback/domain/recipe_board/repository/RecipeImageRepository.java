package com.kjh.boardback.domain.recipe_board.repository;

import com.kjh.boardback.domain.recipe_board.entity.RecipeImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Integer> {

    List<RecipeImage> findByBoard_BoardNumber(Integer boardNumber);

    void deleteByBoard_BoardNumber(Integer boardNumber);
}
