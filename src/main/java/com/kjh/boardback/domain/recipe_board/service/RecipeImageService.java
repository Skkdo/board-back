package com.kjh.boardback.domain.recipe_board.service;

import com.kjh.boardback.domain.recipe_board.entity.RecipeImage;
import com.kjh.boardback.domain.recipe_board.repository.RecipeImageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeImageService {

    private final RecipeImageRepository imageRepository;

    public List<RecipeImage> findByBoardNumber(Integer boardNumber) {
        return imageRepository.findByBoard_BoardNumber(boardNumber);
    }

    public void saveAll(List<RecipeImage> imageList) {
        imageRepository.saveAll(imageList);
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        imageRepository.deleteByBoard_BoardNumber(boardNumber);
    }
}
