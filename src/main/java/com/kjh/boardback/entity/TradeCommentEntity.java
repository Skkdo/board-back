package com.kjh.boardback.entity;

import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "trade_comment")
@Table(name = "trade_comment")
public class TradeCommentEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentNumber;

    private String content;
    private String writeDatetime;
    private String userEmail;
    private int boardNumber;


}
