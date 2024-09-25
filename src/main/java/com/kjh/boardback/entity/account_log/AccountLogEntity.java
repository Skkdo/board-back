package com.kjh.boardback.entity.account_log;

import com.kjh.boardback.dto.request.account_log.PatchAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostAccountLogRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account_log")
@Table(name = "account_log")
public class AccountLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountLogNumber;

    private String userEmail;
    private String content;
    private int type;
    private Integer moneyCustomTypeNumber;
    private int money;
    private String datetime;

    public void patchAccountLog(PatchAccountLogRequestDto dto){
        this.content = dto.getContent();
        this.type = dto.getType();
        this.moneyCustomTypeNumber = dto.getMoneyCustomTypeNumber();
        this.money = dto.getMoney();
    }
    public AccountLogEntity(String email, PostAccountLogRequestDto dto){
        Date now = Date.from(Instant.now());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = simpleDateFormat.format(now);

        this.userEmail = email;
        this.content = dto.getContent();
        this.type = dto.getType();
        this.moneyCustomTypeNumber = dto.getMoneyCustomTypeNumber();
        this.money = dto.getMoney();
        this.datetime = dateTime;
    }
}
