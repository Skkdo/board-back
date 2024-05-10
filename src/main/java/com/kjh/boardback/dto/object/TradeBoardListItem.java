package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.TradeBoardListViewEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TradeBoardListItem {
    private int boardNumber;
    private String title;
    private String content;
    private String boardTitleImage;
    private int favoriteCount;
    private int commentCount;
    private int viewCount;
    private String writeDatetime;
    private String tradeLocation;
    private String writerNickname;
    private String writerProfileImage;

    public TradeBoardListItem(TradeBoardListViewEntity tradeBoardListViewEntity) {
        this.boardNumber = tradeBoardListViewEntity.getBoardNumber();
        this.title = tradeBoardListViewEntity.getTitle();
        this.content = tradeBoardListViewEntity.getContent();
        this.boardTitleImage = tradeBoardListViewEntity.getWriterProfileImage();
        this.favoriteCount = tradeBoardListViewEntity.getFavoriteCount();
        this.commentCount = tradeBoardListViewEntity.getCommentCount();
        this.viewCount = tradeBoardListViewEntity.getViewCount();
        this.writeDatetime = tradeBoardListViewEntity.getWriteDatetime();
        this.tradeLocation = tradeBoardListViewEntity.getTradeLocation();
        this.writerNickname = tradeBoardListViewEntity.getWriterNickname();
        this.writerProfileImage = tradeBoardListViewEntity.getWriterProfileImage();
    }

    public static List<TradeBoardListItem> getList(List<TradeBoardListViewEntity>tradeBoardListViewEntities ){
        List<TradeBoardListItem> tradeBoardListItemList = new ArrayList<>();
        for(TradeBoardListViewEntity tradeBoardListViewEntity : tradeBoardListViewEntities){
            TradeBoardListItem tradeBoardListItem = new TradeBoardListItem(tradeBoardListViewEntity);
            tradeBoardListItemList.add(tradeBoardListItem);
        }
        return tradeBoardListItemList;
    }
}
