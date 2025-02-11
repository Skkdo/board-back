package com.kjh.boardback.entity.recipe_board;

import com.kjh.boardback.dto.object.RecipeStep;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private RecipeBoard board;

    @Column(name = "step", nullable = false)
    private int step;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "content", nullable = true)
    private String content;

    public static RecipeImage from(RecipeBoard board, RecipeStep recipeStep) {
        return RecipeImage.builder()
                .board(board)
                .step(recipeStep.getStep())
                .image(recipeStep.getImage())
                .content(recipeStep.getContent())
                .build();
    }

    public static RecipeImage from(RecipeBoard board, String image) {
        return RecipeImage.builder()
                .board(board)
                .step(0)
                .image(image)
                .build();
    }

    public static List<RecipeImage> toList(RecipeBoard board, List<String> boardImageList, List<RecipeStep> steps) {
        List<RecipeImage> recipeImageList = new ArrayList<>();

        boardImageList.forEach((String image) -> recipeImageList.add(RecipeImage.from(board, image)));
        steps.forEach((RecipeStep step) -> recipeImageList.add(RecipeImage.from(board, step)));
        return recipeImageList;
    }
}
