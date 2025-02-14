package com.kjh.boardback.domain.recipe_board.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeFavoritePk implements Serializable {
    private String userEmail;
    private int boardNumber;
}
