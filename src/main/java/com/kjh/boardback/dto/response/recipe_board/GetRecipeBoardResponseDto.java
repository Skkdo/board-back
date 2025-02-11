package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeStep;
import com.kjh.boardback.entity.recipe_board.RecipeBoard;
import com.kjh.boardback.entity.recipe_board.RecipeImage;
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
    private List<RecipeStep> steps;


    public GetRecipeBoardResponseDto(RecipeBoard board) {
        List<RecipeImage> imageList = board.getImageList();
        List<String> boardImageList = new ArrayList<>();
        List<RecipeStep> steps = new ArrayList<>();

        for (RecipeImage recipeImage : imageList) {
            if (recipeImage.getStep() == 0) {
                boardImageList.add(recipeImage.getImage());
            } else {
                steps.add(new RecipeStep(recipeImage.getStep(), recipeImage.getImage(), recipeImage.getContent()));
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
