package com.kjh.boardback.domain.board.dto.object;

import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.board.entity.Board;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListItem {
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

    public static BoardListItem from(Board board, User user) {
        return BoardListItem.builder()
                .boardNumber(board.getBoardNumber())
                .title(board.getTitle())
                .content(board.getContent())
                .boardTitleImage(board.getTitleImage())
                .favoriteCount(board.getFavoriteCount())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .writeDatetime(board.getCreatedAt())
                .writerNickname(user.getNickname())
                .writerProfileImage(user.getProfileImage())
                .build();
    }

    public static BoardListItem from(Board board) {
        return BoardListItem.builder()
                .boardNumber(board.getBoardNumber())
                .title(board.getTitle())
                .content(board.getContent())
                .boardTitleImage(board.getTitleImage())
                .favoriteCount(board.getFavoriteCount())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .writeDatetime(board.getCreatedAt())
                .writerNickname(board.getWriter().getNickname())
                .writerProfileImage(board.getWriter().getProfileImage())
                .build();
    }

    public static BoardListItem from(BoardDto dto) {
        return BoardListItem.builder()
                .boardNumber(dto.getBoardNumber())
                .title(dto.getTitle())
                .content(dto.getContent())
                .boardTitleImage(dto.getTitleImage())
                .favoriteCount(dto.getFavoriteCount())
                .commentCount(dto.getCommentCount())
                .viewCount(dto.getViewCount())
                .writeDatetime(dto.getCreatedAt())
                .writerNickname(dto.getNickname())
                .writerProfileImage(dto.getProfileImage())
                .build();
    }

    public static List<BoardListItem> getList(List<Board> boardList, User user) {
        return boardList.stream()
                .map(board -> BoardListItem.from(board, user))
                .collect(Collectors.toList());
    }

    public static List<BoardListItem> getList(List<Board> boardList) {
        return boardList.stream()
                .map(BoardListItem::from)
                .collect(Collectors.toList());
    }

    public static Page<BoardListItem> getList(Page<Board> boardList) {
        return boardList.map(BoardListItem::from);
    }

    public static List<BoardListItem> getListByDto(List<BoardDto> dtoList) {
        return dtoList.stream()
                .map(BoardListItem::from)
                .collect(Collectors.toList());
    }
}