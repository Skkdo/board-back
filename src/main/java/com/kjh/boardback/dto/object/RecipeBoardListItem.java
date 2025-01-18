package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.recipe_board.RecipeBoard;
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
    private String writeDatetime;
    private String writerNickname;
    private String writerProfileImage;
    private int type;
    private int cookingTime;
    private String step1_content;
    private String step2_content;
    private String step3_content;
    private String step4_content;
    private String step5_content;
    private String step6_content;
    private String step7_content;
    private String step8_content;

    public RecipeBoardListItem(RecipeBoard board) {
        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardTitleImage = board.getTitleImage();
        this.favoriteCount = board.getFavoriteCount();
        this.commentCount = board.getCommentCount();
        this.viewCount = board.getViewCount();
        this.writeDatetime = board.getCreatedAt().toString();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
        this.cookingTime = board.getCookingTime();
        this.type = board.getType();
        this.step1_content = board.getStep_1();
        this.step2_content = board.getStep_2();
        this.step3_content = board.getStep_3();
        this.step4_content = board.getStep_4();
        this.step5_content = board.getStep_5();
        this.step6_content = board.getStep_6();
        this.step7_content = board.getStep_7();
        this.step8_content = board.getStep_8();
    }

    public static List<RecipeBoardListItem> getList(List<RecipeBoard> boardList) {
        return boardList.stream().map(RecipeBoardListItem::new).toList();
    }
}
