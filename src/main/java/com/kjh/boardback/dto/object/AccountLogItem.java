package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.account_log.AccountLogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountLogItem {
    private int type;
    private String customTypeName;
    private String content;
    private int money;

    public static List<AccountLogItem> getAccountLogItemList(List<AccountLogEntity> accountLogEntityList,List<String> customTypeNameList){
            List<AccountLogItem> accountLogItemList = new ArrayList<>();

            for(int i=0;i< accountLogEntityList.size();i++){
                AccountLogEntity accountLogEntity = accountLogEntityList.get(i);
                String customTypeName = customTypeNameList.get(i);
                AccountLogItem accountLogItem = new AccountLogItem(accountLogEntity, customTypeName);
                accountLogItemList.add(accountLogItem);
            }
            return accountLogItemList;
    }
    public AccountLogItem(AccountLogEntity accountLogEntity,String customTypeName){
        this.type = accountLogEntity.getType();
        this.customTypeName = customTypeName;
        this.content = accountLogEntity.getContent();
        this.money = accountLogEntity.getMoney();
    }
}
