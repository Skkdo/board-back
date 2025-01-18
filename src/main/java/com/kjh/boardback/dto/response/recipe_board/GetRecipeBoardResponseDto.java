package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.entity.recipe_board.RecipeBoard;
import com.kjh.boardback.entity.recipe_board.RecipeImage;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeBoardResponseDto extends ResponseDto {

    private int boardNumber;
    private String title;
    private String content;
    private List<String> boardImageList;
    private String writeDatetime;
    private String writerEmail;
    private String writerNickname;
    private String writerProfileImage;
    private int type;
    private int cookingTime;
    private String step1_image;
    private String step1_content;
    private String step2_image;
    private String step2_content;
    private String step3_image;
    private String step3_content;
    private String step4_image;
    private String step4_content;
    private String step5_image;
    private String step5_content;
    private String step6_image;
    private String step6_content;
    private String step7_image;
    private String step7_content;
    private String step8_image;
    private String step8_content;


    public GetRecipeBoardResponseDto(RecipeBoard board, List<RecipeImage> imageList) {
        super(ResponseCode.SUCCESS);

        List<String> boardImageList = new ArrayList<>();

        classificationImage(imageList, boardImageList);

        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImageList = boardImageList;
        this.writeDatetime = board.getCreatedAt().toString();
        this.writerEmail = board.getWriter().getEmail();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
        this.type = board.getType();
        this.cookingTime = board.getCookingTime();
        this.step1_content = board.getStep_1();
        this.step2_content = board.getStep_2();
        this.step3_content = board.getStep_3();
        this.step4_content = board.getStep_4();
        this.step5_content = board.getStep_5();
        this.step6_content = board.getStep_6();
        this.step7_content = board.getStep_7();
        this.step8_content = board.getStep_8();
    }

    private void classificationImage(List<RecipeImage> imageList, List<String> boardImageList) {
        for (RecipeImage imageEntity : imageList) {
            String boardImage = imageEntity.getImage();
            int step = imageEntity.getStep();
            switch (step) {
                case 0:
                    boardImageList.add(boardImage);
                    break;
                case 1:
                    this.step1_image = boardImage;
                    break;
                case 2:
                    this.step2_image = boardImage;
                    break;
                case 3:
                    this.step3_image = boardImage;
                    break;
                case 4:
                    this.step4_image = boardImage;
                    break;
                case 5:
                    this.step5_image = boardImage;
                    break;
                case 6:
                    this.step6_image = boardImage;
                    break;
                case 7:
                    this.step7_image = boardImage;
                    break;
                case 8:
                    this.step8_image = boardImage;
                    break;
            }
        }
    }

}
