package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.object.FavoriteListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.repository.resultSet.GetFavoriteListResultSet;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetFavoriteListResponseDto extends ResponseDto {

    private final List<FavoriteListItem> favoriteList;

    private GetFavoriteListResponseDto(List<GetFavoriteListResultSet> resultSets) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.favoriteList = FavoriteListItem.copyList(resultSets);
    }

    public static ResponseEntity<GetFavoriteListResponseDto> success(List<GetFavoriteListResultSet> resultSets) {
        GetFavoriteListResponseDto result = new GetFavoriteListResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
