package com.kjh.boardback.domain.trade_board.dto.object;

import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TradeBoardListItem {
    private int boardNumber;
    private String title;
    private String content;
    private String boardTitleImage;
    private int favoriteCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime writeDatetime;
    private String tradeLocation;
    private String price;
    private String writerNickname;
    private String writerProfileImage;

    public TradeBoardListItem(TradeBoard board) {
        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardTitleImage = board.getTitleImage();
        this.favoriteCount = board.getFavoriteCount();
        this.commentCount = board.getCommentCount();
        this.viewCount = board.getViewCount();
        this.writeDatetime = board.getCreatedAt();
        this.tradeLocation = board.getTradeLocation();
        this.price = board.getPrice();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
    }

    public static List<TradeBoardListItem> getList(List<TradeBoard> latestList) {
        return latestList.stream().map(TradeBoardListItem::new).toList();
    }
}
