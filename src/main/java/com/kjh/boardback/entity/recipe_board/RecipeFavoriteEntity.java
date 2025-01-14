package com.kjh.boardback.entity.recipe_board;

import com.kjh.boardback.entity.compositeKey.RecipeFavoritePk;
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
@Entity(name = "recipe_favorite")
@Table(name = "recipe_favorite")
@IdClass(RecipeFavoritePk.class)
public class RecipeFavoriteEntity {
    @Id
    private String userEmail;
    @Id
    private int boardNumber;
    
}
