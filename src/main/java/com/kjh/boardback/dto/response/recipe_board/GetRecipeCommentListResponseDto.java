package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseMessage;
import com.kjh.boardback.dto.object.RecipeCommentListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.repository.resultSet.GetRecipeCommentListResultSet;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetRecipeCommentListResponseDto extends ResponseDto {

    private List<RecipeCommentListItem> commentList;

    private GetRecipeCommentListResponseDto(List<GetRecipeCommentListResultSet> resultSets) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.commentList = RecipeCommentListItem.copyList(resultSets);
    }

    public static ResponseEntity<GetRecipeCommentListResponseDto> success(List<GetRecipeCommentListResultSet> resultSets){
        GetRecipeCommentListResponseDto result = new GetRecipeCommentListResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard(){
        ResponseDto result  = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
