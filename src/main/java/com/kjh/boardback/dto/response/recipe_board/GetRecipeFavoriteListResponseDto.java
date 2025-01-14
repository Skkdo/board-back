package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.object.RecipeFavoriteListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.repository.resultSet.GetRecipeFavoriteListResultSet;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetRecipeFavoriteListResponseDto extends ResponseDto {

    private final List<RecipeFavoriteListItem> favoriteList;

    private GetRecipeFavoriteListResponseDto(List<GetRecipeFavoriteListResultSet> resultSets) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.favoriteList = RecipeFavoriteListItem.copyList(resultSets);
    }

    public static ResponseEntity<GetRecipeFavoriteListResponseDto> success(List<GetRecipeFavoriteListResultSet> resultSets) {
        GetRecipeFavoriteListResponseDto result = new GetRecipeFavoriteListResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
