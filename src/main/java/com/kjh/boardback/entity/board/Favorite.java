package com.kjh.boardback.entity.board;

import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.primaryKey.FavoritePk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @EmbeddedId
    private FavoritePk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userEmail")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("boardNumber")
    @JoinColumn(name = "board_number", nullable = false)
    private Board board;
}
