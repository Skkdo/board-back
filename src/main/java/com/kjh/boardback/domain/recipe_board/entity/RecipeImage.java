package com.kjh.boardback.domain.recipe_board.entity;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeStepItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private RecipeBoard board;

    @Column(name = "step", nullable = false)
    private int step;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "content", nullable = true)
    private String content;

    public static RecipeImage fromStepImage(RecipeBoard board, RecipeStepItem recipeStep) {
        return RecipeImage.builder()
                .board(board)
                .step(recipeStep.getStep())
                .image(recipeStep.getImage())
                .content(recipeStep.getContent())
                .build();
    }

    public static RecipeImage fromBoardImage(RecipeBoard board, String image) {
        return RecipeImage.builder()
                .board(board)
                .step(0)
                .image(image)
                .build();
    }

    public static List<RecipeImage> toList(RecipeBoard board, List<String> boardImageList, List<RecipeStepItem> steps) {
        List<RecipeImage> recipeImageList = new ArrayList<>();

        boardImageList.forEach((String image) -> recipeImageList.add(RecipeImage.fromBoardImage(board, image)));
        steps.forEach((RecipeStepItem step) -> recipeImageList.add(RecipeImage.fromStepImage(board, step)));
        return recipeImageList;
    }
}
