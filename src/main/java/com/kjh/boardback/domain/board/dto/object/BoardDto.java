package com.kjh.boardback.domain.board.dto.object;

import com.kjh.boardback.domain.board.entity.Board;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {
    private int boardNumber;
    private String title;
    private String content;
    private String titleImage;
    private int favoriteCount;
    private int commentCount;
    private int viewCount;
    private LocalDateTime createdAt;

    // Writer
    private String email;
    private String password;
    private String nickname;
    private String telNumber;
    private String address;
    private String addressDetail;
    private String profileImage;
    private boolean agreedPersonal;

    public static BoardDto from(Board board) {
        return BoardDto.builder()
                .boardNumber(board.getBoardNumber())
                .title(board.getTitle())
                .content(board.getContent())
                .titleImage(board.getTitleImage())
                .favoriteCount(board.getFavoriteCount())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .createdAt(board.getCreatedAt())
                // writer
                .email(board.getWriter().getEmail())
                .password(board.getWriter().getPassword())
                .nickname(board.getWriter().getNickname())
                .telNumber(board.getWriter().getTelNumber())
                .address(board.getWriter().getAddress())
                .addressDetail(board.getWriter().getAddressDetail())
                .profileImage(board.getWriter().getProfileImage())
                .agreedPersonal(board.getWriter().isAgreedPersonal())
                .build();
    }

    public static List<BoardDto> getList(List<Board> boardList) {
        return boardList.stream().map(BoardDto::from).toList();
    }
}
