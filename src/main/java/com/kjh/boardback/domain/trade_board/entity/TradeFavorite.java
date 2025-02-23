package com.kjh.boardback.domain.trade_board.entity;

import com.kjh.boardback.domain.user.entity.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
public class TradeFavorite {
    @EmbeddedId
    private TradeFavoritePk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userEmail")
    @JoinColumn(name = "user_email", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardNumber")
    @JoinColumn(name = "board_number", nullable = false)
    private TradeBoard board;

    public static TradeFavorite from(User user, TradeBoard board) {
        return TradeFavorite.builder()
                .user(user)
                .board(board)
                .build();
    }
}
