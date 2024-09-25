package com.kjh.boardback.entity.account_log;

import com.kjh.boardback.dto.request.account_log.PostMoneyCustomTypeRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "money_custom_type")
@Table(name = "money_custom_type")
public class MoneyCustomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customTypeNumber;
    private String userEmail;
    private String customTypeName;

    public MoneyCustomTypeEntity(String email,PostMoneyCustomTypeRequestDto dto){
        this.userEmail = email;
        this.customTypeName = dto.getCustomTypeName();
    }
}
