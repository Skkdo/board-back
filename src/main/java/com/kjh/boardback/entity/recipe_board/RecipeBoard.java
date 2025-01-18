package com.kjh.boardback.entity.recipe_board;

import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
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
@SQLDelete(sql = "UPDATE recipe_board SET is_deleted = true WHERE board_number = ?")
public class RecipeBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "favoriteCount", nullable = false)
    private int favoriteCount;

    @Column(name = "commentCount", nullable = false)
    private int commentCount;

    @Column(name = "viewCount", nullable = false)
    private int viewCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_email", nullable = false)
    private User writer;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "cookingTime", nullable = false)
    private int cookingTime;

    @Column(name = "title_image", nullable = true)
    private String titleImage;

    @Column(name = "step_1", nullable = true)
    private String step_1;

    @Column(name = "step_2", nullable = true)
    private String step_2;

    @Column(name = "step_3", nullable = true)
    private String step_3;

    @Column(name = "step_4", nullable = true)
    private String step_4;

    @Column(name = "step_5", nullable = true)
    private String step_5;

    @Column(name = "step_6", nullable = true)
    private String step_6;

    @Column(name = "step_7", nullable = true)
    private String step_7;

    @Column(name = "step_8", nullable = true)
    private String step_8;


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

    public static RecipeBoard from(PostRecipeBoardRequestDto dto, User user) {
        String titleImage = null;
        if(!dto.getBoardImageList().isEmpty()){
            titleImage = dto.getBoardImageList().get(0);
        }
        return RecipeBoard.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .favoriteCount(0)
                .commentCount(0)
                .viewCount(0)
                .writer(user)
                .type(dto.getType())
                .cookingTime(dto.getCookingTime())
                .titleImage(titleImage)
                .step_1(dto.getStep1_content())
                .step_2(dto.getStep2_content())
                .step_3(dto.getStep3_content())
                .step_4(dto.getStep4_content())
                .step_5(dto.getStep5_content())
                .step_6(dto.getStep6_content())
                .step_7(dto.getStep7_content())
                .step_8(dto.getStep8_content())
                .build();
    }

    public void patch(PatchRecipeBoardRequestDto dto) {
        String titleImage = null;
        if(!dto.getBoardImageList().isEmpty()){
            titleImage = dto.getBoardImageList().get(0);
        }

        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.type = dto.getType();
        this.cookingTime = dto.getCookingTime();
        this.titleImage = titleImage;

        this.step_1 = dto.getStep1_content();
        this.step_2 = dto.getStep2_content();
        this.step_3 = dto.getStep3_content();
        this.step_4 = dto.getStep4_content();
        this.step_5 = dto.getStep5_content();
        this.step_6 = dto.getStep6_content();
        this.step_7 = dto.getStep7_content();
        this.step_8 = dto.getStep8_content();
    }
}
