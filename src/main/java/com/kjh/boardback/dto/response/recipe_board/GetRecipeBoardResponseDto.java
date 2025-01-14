package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.recipe_board.RecipeImageEntity;
import com.kjh.boardback.repository.resultSet.GetRecipeBoardResultSet;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

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
   

    private GetRecipeBoardResponseDto(GetRecipeBoardResultSet resultSet, List<RecipeImageEntity> imageEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        List<String> boardImageList = new ArrayList<>();

        classificationImage(imageEntities, boardImageList);

        this.boardNumber = resultSet.getBoardNumber();
        this.title = resultSet.getTitle();
        this.content = resultSet.getContent();
        this.boardImageList = boardImageList;
        this.writeDatetime = resultSet.getWriteDatetime();
        this.writerEmail = resultSet.getWriterEmail();
        this.writerNickname = resultSet.getWriterNickname();
        this.writerProfileImage = resultSet.getWriterProfileImage();
        this.type = resultSet.getType();
        this.cookingTime = resultSet.getCookingTime();
        this.step1_content = resultSet.getStep_1();
        this.step2_content = resultSet.getStep_2();
        this.step3_content = resultSet.getStep_3();
        this.step4_content = resultSet.getStep_4();
        this.step5_content = resultSet.getStep_5();
        this.step6_content = resultSet.getStep_6();
        this.step7_content = resultSet.getStep_7();
        this.step8_content = resultSet.getStep_8();
    }

    private void classificationImage(List<RecipeImageEntity> imageEntities, List<String> boardImageList) {
        for(RecipeImageEntity imageEntity : imageEntities){
            String boardImage = imageEntity.getImage();
            int step = imageEntity.getStep();
            switch(step){
                case 0:
                    boardImageList.add(boardImage);
                    break;
                case 1:
                    this.step1_image=boardImage;
                    break;
                case 2:
                    this.step2_image=boardImage;
                    break;
                case 3:
                    this.step3_image=boardImage;
                    break;
                case 4:
                    this.step4_image=boardImage;
                    break;
                case 5:
                    this.step5_image=boardImage;
                    break;
                case 6:
                    this.step6_image=boardImage;
                    break;
                case 7:
                    this.step7_image=boardImage;
                    break;
                case 8:
                    this.step8_image=boardImage;
                    break;
            }
        }
    }

    public static ResponseEntity<GetRecipeBoardResponseDto> success(GetRecipeBoardResultSet resultSet, List<RecipeImageEntity> imageEntities){
        GetRecipeBoardResponseDto result = new GetRecipeBoardResponseDto(resultSet,imageEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
