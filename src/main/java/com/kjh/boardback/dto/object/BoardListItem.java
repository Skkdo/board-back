package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.board.Board;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String writeDatetime;
    private String writerNickname;
    private String writerProfileImage;

    public BoardListItem(Board board, User user) {
        BoardListItem.builder()
                .boardNumber(board.getBoardNumber())
                .title(board.getTitle())
                .content(board.getContent())
                .boardTitleImage(board.getTitleImage())
                .favoriteCount(board.getFavoriteCount())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .writeDatetime(board.getCreatedAt().toString())
                .writerNickname(user.getNickname())
                .writerProfileImage(user.getProfileImage());
    }

    public BoardListItem(Board board) {
        BoardListItem.builder()
                .boardNumber(board.getBoardNumber())
                .title(board.getTitle())
                .content(board.getContent())
                .boardTitleImage(board.getTitleImage())
                .favoriteCount(board.getFavoriteCount())
                .commentCount(board.getCommentCount())
                .viewCount(board.getViewCount())
                .writeDatetime(board.getCreatedAt().toString())
                .writerNickname(board.getWriter().getNickname())
                .writerProfileImage(board.getWriter().getProfileImage());
    }

    public static List<BoardListItem> getList(List<Board> boardList, User user) {
        return boardList.stream()
                .map(board -> {
                    return new BoardListItem(board, user);
                })
                .collect(Collectors.toList());
    }

    public static List<BoardListItem> getList(List<Board> boardList) {
        return boardList.stream()
                .map(board -> {
                    return new BoardListItem(board);
                })
                .collect(Collectors.toList());
    }
}