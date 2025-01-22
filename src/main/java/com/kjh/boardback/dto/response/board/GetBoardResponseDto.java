package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.entity.board.Image;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class GetBoardResponseDto {

    private int boardNumber;
    private String title;
    private String content;
    private List<String> boardImageList;
    private LocalDateTime writeDatetime;
    private String writerEmail;
    private String writerNickname;
    private String writerProfileImage;

    public GetBoardResponseDto(Board board, List<Image> imageList) {

        List<String> boardImageList = new ArrayList<>();
        for (Image image : imageList) {
            String boardImage = image.getImage();
            boardImageList.add(boardImage);
        }

        this.boardNumber = board.getBoardNumber();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImageList = boardImageList;
        this.writeDatetime = board.getCreatedAt();
        this.writerEmail = board.getWriter().getEmail();
        this.writerNickname = board.getWriter().getNickname();
        this.writerProfileImage = board.getWriter().getProfileImage();
    }
}
