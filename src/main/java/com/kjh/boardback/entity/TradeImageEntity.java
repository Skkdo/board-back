package com.kjh.boardback.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "trade_image")
@Table(name = "trade_image")
public class TradeImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sequence;
    private int boardNumber;
    private String image;


    public TradeImageEntity(int boardNumber, String image) {
        this.boardNumber = boardNumber;
        this.image = image;
    }
}
