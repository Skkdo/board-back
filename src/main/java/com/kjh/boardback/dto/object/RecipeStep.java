package com.kjh.boardback.dto.object;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeStep {
    private int step;
    private String image;
    private String content;
}
