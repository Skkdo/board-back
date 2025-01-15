package com.kjh.boardback.repository.recipe_board;

import com.kjh.boardback.entity.recipe_board.RecipeImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Integer> {

    List<RecipeImage> findByBoard_BoardNumber(Integer boardNumber);

    void deleteByBoard_BoardNumber(Integer boardNumber);
}
