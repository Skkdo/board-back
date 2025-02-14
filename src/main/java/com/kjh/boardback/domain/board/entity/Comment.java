package com.kjh.boardback.domain.board.entity;

import com.kjh.boardback.domain.board.dto.request.PatchCommentRequestDto;
import com.kjh.boardback.domain.board.dto.request.PostCommentRequestDto;
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
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE comment_number = ?")
public class Comment extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_number")
    private int commentNumber;

    @Column(name = "content" , nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_email" , nullable = false)
    private User writer;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "board_number" , nullable = false)
    private Board board;

    public void patchComment(PatchCommentRequestDto dto){
        this.content = dto.getContent();
    }

    public Comment(Board board, User user, PostCommentRequestDto dto){
        this.content = dto.getContent();
        this.writer = user;
        this.board = board;
    }
}
