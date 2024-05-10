package com.kjh.boardback.entity;

import com.kjh.boardback.entity.primaryKey.FavoritePk;
import com.kjh.boardback.entity.primaryKey.TradeFavoritePk;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "trade_favorite")
@Table(name = "trade_favorite")
@IdClass(TradeFavoritePk.class)
public class TradeFavoriteEntity {
    @Id
    private String userEmail;
    @Id
    private int boardNumber;
    
}
