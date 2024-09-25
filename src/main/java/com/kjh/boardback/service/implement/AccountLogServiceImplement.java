package com.kjh.boardback.service.implement;

import com.kjh.boardback.dto.object.AccountLogItem;
import com.kjh.boardback.dto.object.DayAccountLogItem;
import com.kjh.boardback.dto.request.account_log.PatchAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostMoneyCustomTypeRequestDto;
import com.kjh.boardback.dto.response.account_log.*;
import com.kjh.boardback.entity.account_log.AccountLogEntity;
import com.kjh.boardback.entity.account_log.MoneyCustomTypeEntity;
import com.kjh.boardback.repository.UserRepository;
import com.kjh.boardback.repository.account_log.AccountLogRepository;
import com.kjh.boardback.repository.account_log.MoneyCustomTypeRepository;
import com.kjh.boardback.service.AccountLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountLogServiceImplement implements AccountLogService {
    private final UserRepository userRepository;
    private final AccountLogRepository accountLogRepository;
    private final MoneyCustomTypeRepository moneyCustomTypeRepository;
    @Override
    public ResponseEntity<? super DeleteAccountLogResponseDto> deleteAccountLog(String email,int accountLogNumber) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return DeleteAccountLogResponseDto.noExistUser();
            }
            Boolean existedAccountLog = accountLogRepository.existsByAccountLogNumber(accountLogNumber);
            if(!existedAccountLog){
                return DeleteAccountLogResponseDto.noExistBoard();
            }
            AccountLogEntity accountLogEntity = accountLogRepository.findByAccountLogNumber(accountLogNumber);
            String userEmail = accountLogEntity.getUserEmail();
            if(!userEmail.equals(email)){
                return DeleteAccountLogResponseDto.noPermission();
            }
            accountLogRepository.deleteByAccountLogNumber(accountLogNumber);

        }catch (Exception exception){
            exception.printStackTrace();
            return DeleteAccountLogResponseDto.databaseError();
        }
        return DeleteAccountLogResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteMonetCustomTypeResponseDto> deleteMoneyCustomType(String email, int customTypeNumber) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return DeleteMonetCustomTypeResponseDto.noExistUser();
            }
            Boolean existedAccountLog = moneyCustomTypeRepository.existsByCustomTypeNumber(customTypeNumber);
            if(!existedAccountLog){
                return DeleteMonetCustomTypeResponseDto.noExistBoard();
            }
            MoneyCustomTypeEntity moneyCustomTypeEntity = moneyCustomTypeRepository.findByCustomTypeNumber(customTypeNumber);
            String userEmail = moneyCustomTypeEntity.getUserEmail();
            if(!userEmail.equals(email)){
                return DeleteMonetCustomTypeResponseDto.noPermission();
            }
            moneyCustomTypeRepository.deleteByCustomTypeNumber(customTypeNumber);
        }catch (Exception exception){
            exception.printStackTrace();
            return DeleteMonetCustomTypeResponseDto.databaseError();
        }
        return DeleteMonetCustomTypeResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchAccountLogResponseDto> patchAccountLog(String email, int accountLogNumber,PatchAccountLogRequestDto dto) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return PatchAccountLogResponseDto.noExistUser();
            }
            Boolean existedAccountLog = accountLogRepository.existsByAccountLogNumber(accountLogNumber);
            if(!existedAccountLog){
                return PatchAccountLogResponseDto.noExistBoard();
            }
            AccountLogEntity accountLogEntity = accountLogRepository.findByAccountLogNumber(accountLogNumber);
            String userEmail = accountLogEntity.getUserEmail();
            if(!userEmail.equals(email)){
                return PatchAccountLogResponseDto.noPermission();
            }
            accountLogEntity.patchAccountLog(dto);
            accountLogRepository.save(accountLogEntity);

        }catch (Exception exception){
            exception.printStackTrace();
            return PatchAccountLogResponseDto.databaseError();
        }
        return PatchAccountLogResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostMoneyCustomTypeResponseDto> postMoneyCustomType(String email, PostMoneyCustomTypeRequestDto dto) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return PostMoneyCustomTypeResponseDto.noExistUser();
            }
            MoneyCustomTypeEntity moneyCustomTypeEntity = new MoneyCustomTypeEntity(email, dto);
            moneyCustomTypeRepository.save(moneyCustomTypeEntity);
        }catch (Exception exception){
            exception.printStackTrace();
            return PostMoneyCustomTypeResponseDto.databaseError();
        }
        return PostMoneyCustomTypeResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostAccountLogResponseDto> postAccountLog(String email, PostAccountLogRequestDto dto) {
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return PostAccountLogResponseDto.noExistUser();
            }
            AccountLogEntity accountLogEntity = new AccountLogEntity(email, dto);
            accountLogRepository.save(accountLogEntity);
        }catch (Exception exception){
            exception.printStackTrace();
            return PostAccountLogResponseDto.databaseError();
        }
        return PostAccountLogResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetMoneyCustomTypeResponseDto> getMoneyCustomType(String email) {
        List<MoneyCustomTypeEntity> moneyCustomTypeEntityList = new ArrayList<>();
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return GetMoneyCustomTypeResponseDto.noExistUser();
            }
            moneyCustomTypeEntityList = moneyCustomTypeRepository.findByUserEmail(email);
        }catch (Exception exception){
            exception.printStackTrace();
            return GetMoneyCustomTypeResponseDto.databaseError();
        }
        return GetMoneyCustomTypeResponseDto.success(moneyCustomTypeEntityList);
    }

    @Override
    public ResponseEntity<? super GetDayAccountLogResponseDto> getDayAccountLog(String email, String datetime) {
        List<AccountLogItem> accountLogItemList= new ArrayList<>();
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return GetDayAccountLogResponseDto.noExistUser();
            }
            String startDatetime = datetime +" 00:00:00";
            String endDatetime = datetime +" 23:59:59";
            List<AccountLogEntity> DayAccountLogEntityList = accountLogRepository.getAccountLogByDatetime(startDatetime, endDatetime,email);

            List<String> customTypeNameList = new ArrayList<>();
            for(AccountLogEntity accountLogEntity : DayAccountLogEntityList){
                if(accountLogEntity.getMoneyCustomTypeNumber()==null){
                    customTypeNameList.add("");
                    continue;
                }
                int moneyCustomTypeNumber = accountLogEntity.getMoneyCustomTypeNumber();
                MoneyCustomTypeEntity moneyCustomTypeEntity = moneyCustomTypeRepository.findByUserEmailAndCustomTypeNumber(email, moneyCustomTypeNumber);
                String customTypeName = moneyCustomTypeEntity.getCustomTypeName();
                customTypeNameList.add(customTypeName);
            }

            accountLogItemList=AccountLogItem.getAccountLogItemList(DayAccountLogEntityList,customTypeNameList);

        }catch (Exception exception){
            exception.printStackTrace();
            return GetDayAccountLogResponseDto.databaseError();
        }
        return GetDayAccountLogResponseDto.success(accountLogItemList);
    }

    @Override
    public ResponseEntity<? super GetCalenderResponseDto> getCalender(String email, String datetime) {
        String subString = datetime.substring(0,7);
        String startDatetime = subString +"-01 00:00:00";

        String stringMonth = datetime.substring(5,7);
        int month = Integer.parseInt(stringMonth);
        String endDatetime = getDaysByMonth(month, subString);

        String dayString = endDatetime.substring(8,10);
        int day = Integer.parseInt(dayString);

        List<DayAccountLogItem> calender = new ArrayList<>(day);
        for(int i=1;i<day+1;i++){
            calender.add(new DayAccountLogItem(i));
        }
        try{
            boolean existedEmail = userRepository.existsByEmail(email);
            if(!existedEmail){
                return GetCalenderResponseDto.noExistUser();
            }

            List<AccountLogEntity> monthAccountLogEntityList = accountLogRepository.getAccountLogByDatetime(startDatetime, endDatetime,email);
            for(AccountLogEntity accountLogEntity : monthAccountLogEntityList){

                String getDatetime = accountLogEntity.getDatetime();
                String stringDate = getDatetime.substring(8, 10);
                int date = Integer.parseInt(stringDate);
                int type = accountLogEntity.getType();
                int money = accountLogEntity.getMoney();

                DayAccountLogItem simpleLogItem = calender.get(date-1);
                if(type==0){
                    Integer income = simpleLogItem.getTotalIncome();
                    income += money;
                    simpleLogItem.setTotalIncome(income);
                }
                if(type == 1){
                    Integer expense = simpleLogItem.getTotalExpense();
                    expense -= money;
                    simpleLogItem.setTotalExpense(expense);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
            return GetCalenderResponseDto.databaseError();
        }
        return GetCalenderResponseDto.success(calender);
    }
    private String getDaysByMonth(int month,String subString){
        String endDatetime= "";
        switch (month){
            case 1:
                endDatetime = subString +"-31 23:59:59";
                break;
            case 2:
                endDatetime = subString +"-27 23:59:59";
                break;
            case 3:
                endDatetime = subString +"-31 23:59:59";
                break;
            case 4:
                endDatetime = subString +"-30 23:59:59";
                break;
            case 5:
                endDatetime = subString +"-31 23:59:59";
                break;
            case 6:
                endDatetime = subString +"-30 23:59:59";
                break;
            case 7:
                endDatetime = subString +"-31 23:59:59";
                break;
            case 8:
                endDatetime = subString +"-31 23:59:59";
                break;
            case 9:
                endDatetime = subString +"-30 23:59:59";
                break;
            case 10:
                endDatetime = subString +"-31 23:59:59";
                break;
            case 11:
                endDatetime = subString +"-30 23:59:59";
                break;
            case 12:
                endDatetime = subString +"-31 23:59:59";
                break;
        }
        return endDatetime;
    }
}
