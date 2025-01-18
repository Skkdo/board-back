package com.kjh.boardback.entity.trade_board;

import com.kjh.boardback.dto.request.trade_board.PatchTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeBoardRequestDto;
import com.kjh.boardback.entity.BaseEntity;
import com.kjh.boardback.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE trade_board SET is_deleated = true WHERE board_number = ?")
public class TradeBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "title_umage", nullable = true)
    private String titleImage;

    @Column(name = "favoriteCount", nullable = false)
    private int favoriteCount;

    @Column(name = "commentCount", nullable = false)
    private int commentCount;

    @Column(name = "viewCount", nullable = false)
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_email")
    private User writer;

    @Column(name = "tradeLocation", nullable = true)
    private String tradeLocation;

    @Column(name = "price", nullable = true)
    private String price;


    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

    public void increaseFavoriteCount() {
        this.favoriteCount++;
    }

    public void decreaseFavoriteCount() {
        this.favoriteCount--;
    }

    public void patchBoard(PatchTradeBoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.tradeLocation = requestDto.getTradeLocation();
        this.price = requestDto.getPrice();
    }

    public static TradeBoard from(PostTradeBoardRequestDto dto, User user) {
        String titleImage = null;
        if (!dto.getBoardImageList().isEmpty()){
            titleImage = dto.getBoardImageList().get(0);
        }
        return TradeBoard.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .titleImage(titleImage)
                .favoriteCount(0)
                .commentCount(0)
                .viewCount(0)
                .writer(user)
                .tradeLocation(dto.getTradeLocation())
                .price(dto.getPrice())
                .build();
    }

}
