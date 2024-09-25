package com.kjh.boardback.dto.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DayAccountLogItem {
    private Integer day;
    private Integer totalIncome;
    private Integer totalExpense;

    public DayAccountLogItem(int day){
        this.totalIncome=0;
        this.totalExpense=0;
        this.day =day;
    }
}
