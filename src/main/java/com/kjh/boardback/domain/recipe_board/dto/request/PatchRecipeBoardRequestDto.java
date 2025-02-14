package com.kjh.boardback.domain.recipe_board.dto.request;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeStepItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatchRecipeBoardRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotNull
    private List<String> boardImageList;
    @NotNull
    private int type;
    @NotNull
    private int cookingTime;

    private List<RecipeStepItem> steps;
}
