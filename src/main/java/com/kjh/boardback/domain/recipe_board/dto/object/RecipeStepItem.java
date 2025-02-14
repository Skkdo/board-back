package com.kjh.boardback.domain.recipe_board.dto.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeStepItem {
    private int step;
    private String image;
    private String content;
}
