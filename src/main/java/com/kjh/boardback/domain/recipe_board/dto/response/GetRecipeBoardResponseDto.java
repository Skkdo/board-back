package com.kjh.boardback.domain.recipe_board.dto.response;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeStepItem;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeBoardResponseDto {

    private int boardNumber;
    private String title;
    private String content;
    private List<String> boardImageList;
    private LocalDateTime writeDatetime;
    private String writerEmail;
    private String writerNickname;
    private String writerProfileImage;
    private int type;
    private int cookingTime;
    private List<RecipeStepItem> steps;


    public GetRecipeBoardResponseDto(RecipeBoard board, List<RecipeImage> imageList) {
        List<String> boardImageList = new ArrayList<>();
        List<RecipeStepItem> steps = new ArrayList<>();

        for (RecipeImage recipeImage : imageList) {
            if (recipeImage.getStep() == 0) {
                boardImageList.add(recipeImage.getImage());
            } else {
                steps.add(new RecipeStepItem(recipeImage.getStep(), recipeImage.getImage(), recipeImage.getContent()));
            }
        }

        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImageList = boardImageList;
        this.writeDatetime = board.getCreatedAt();
        this.writerEmail = board.getWriter().getEmail();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
        this.type = board.getType();
        this.cookingTime = board.getCookingTime();
        this.steps = steps;
    }

}
