package com.kjh.boardback.entity.recipe_board;

import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.entity.BaseEntity;
import com.kjh.boardback.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PreRemove;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE recipe_board SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE board_number = ?")
public class RecipeBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardNumber;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_email", nullable = false)
    private User writer;

    @Column(name = "title_image", nullable = true)
    private String titleImage;

    @Setter
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("step ASC")
    private List<RecipeImage> imageList;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "cookingTime", nullable = false)
    private int cookingTime;

    @Column(name = "favoriteCount", nullable = false)
    private int favoriteCount;

    @Column(name = "commentCount", nullable = false)
    private int commentCount;

    @Column(name = "viewCount", nullable = false)
    private int viewCount;

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
    }

}
