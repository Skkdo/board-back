package com.kjh.boardback.domain.board.entity;


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

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sequence")
    private int sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_number", nullable = false)
    private Board board;

    @Column(name = "image", nullable = false)
    private String image;

    public static Image from(Board board, String image) {
        return Image.builder()
                .board(board)
                .image(image)
                .build();
    }
}
