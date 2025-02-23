package com.kjh.boardback.domain.trade_board.dto.response;

import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import com.kjh.boardback.domain.trade_board.entity.TradeImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeBoardResponseDto {

    private final int boardNumber;
    private final String title;
    private final String content;
    private final List<String> boardImageList;
    private final LocalDateTime writeDatetime;
    private final String writerEmail;
    private final String tradeLocation;
    private final String price;
    private final String writerNickname;
    private final String writerProfileImage;

    public GetTradeBoardResponseDto(TradeBoard board, List<TradeImage> imageList) {

        List<String> boardImageList = new ArrayList<>();
        for (TradeImage imageEntity : imageList) {
            String boardImage = imageEntity.getImage();
            boardImageList.add(boardImage);
        }

        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImageList = boardImageList;
        this.writeDatetime = board.getCreatedAt();
        this.writerEmail = board.getWriter().getEmail();
        this.tradeLocation = board.getTradeLocation();
        this.price = board.getPrice();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
    }

}
