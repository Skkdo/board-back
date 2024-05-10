package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.common.ResponseCode;
import com.kjh.boardback.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.TradeImageEntity;
import com.kjh.boardback.repository.resultSet.GetTradeBoardResultSet;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetTradeBoardResponseDto extends ResponseDto {

    private final int boardNumber;
    private final String title;
    private final String content;
    private final List<String> boardImageList;
    private final String writeDatetime;
    private final String writerEmail;
    private final String tradeLocation;
    private final String writerNickname;
    private final String writerProfileImage;

    private GetTradeBoardResponseDto(GetTradeBoardResultSet resultSet, List<TradeImageEntity> imageEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

        List<String> boardImageList = new ArrayList<>();
        for (TradeImageEntity imageEntity : imageEntities) {
            String boardImage = imageEntity.getImage();
            boardImageList.add(boardImage);
        }

        this.boardNumber = resultSet.getBoardNumber();
        this.title = resultSet.getTitle();
        this.content = resultSet.getContent();
        this.boardImageList = boardImageList;
        this.writeDatetime = resultSet.getWriteDatetime();
        this.writerEmail = resultSet.getWriterEmail();
        this.tradeLocation = resultSet.getTradeLocation();
        this.writerNickname = resultSet.getWriterNickname();
        this.writerProfileImage = resultSet.getWriterProfileImage();
    }

    public static ResponseEntity<GetTradeBoardResponseDto> success(GetTradeBoardResultSet resultSet, List<TradeImageEntity> imageEntities) {
        GetTradeBoardResponseDto result = new GetTradeBoardResponseDto(resultSet, imageEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }


}
