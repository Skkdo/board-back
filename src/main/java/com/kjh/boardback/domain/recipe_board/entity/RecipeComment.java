package com.kjh.boardback.domain.recipe_board.entity;

import com.kjh.boardback.domain.recipe_board.dto.request.PatchRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.request.PostRecipeCommentRequestDto;
import com.kjh.boardback.global.entity.BaseEntity;
import com.kjh.boardback.domain.user.entity.User;
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
@SQLDelete(sql = "UPDATE recipe_comment SET is_deleted = true WHERE comment_number = ?")
public class RecipeComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentNumber;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_email", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_number", nullable = false)
    private RecipeBoard board;

    public void patchComment(PatchRecipeCommentRequestDto dto) {
        this.content = dto.getContent();
    }

    public static RecipeComment from(RecipeBoard board, User user, PostRecipeCommentRequestDto dto) {
        return RecipeComment.builder()
                .board(board)
                .writer(user)
                .content(dto.getContent())
                .build();
    }
}
