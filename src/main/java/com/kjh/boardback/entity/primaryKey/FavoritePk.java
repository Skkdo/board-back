package com.kjh.boardback.entity.primaryKey;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePk implements Serializable {
    private String userEmail;
    private long boardNumber;
}
