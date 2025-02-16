package com.kjh.boardback.domain.recipe_board.dto.object;

import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RecipeBoardListItem {
    private int boardNumber;
    private String title;
    private String content;
    private String boardTitleImage;
    private int favoriteCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime writeDatetime;
    private String writerNickname;
    private String writerProfileImage;
    private int type;
    private int cookingTime;

    public RecipeBoardListItem(RecipeBoard board) {
        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardTitleImage = board.getTitleImage();
        this.favoriteCount = board.getFavoriteCount();
        this.commentCount = board.getCommentCount();
        this.viewCount = board.getViewCount();
        this.writeDatetime = board.getCreatedAt();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
        this.cookingTime = board.getCookingTime();
        this.type = board.getType();
    }

    public static List<RecipeBoardListItem> getList(List<RecipeBoard> boardList) {
        return boardList.stream().map(RecipeBoardListItem::new).toList();
    }
}
