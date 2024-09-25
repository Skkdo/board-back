package com.kjh.boardback.repository.account_log;

import com.kjh.boardback.entity.account_log.AccountLogEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountLogRepository extends JpaRepository<AccountLogEntity,Integer> {
    Boolean existsByAccountLogNumber(Integer accountLogNumber);
    AccountLogEntity findByAccountLogNumber(Integer accountLogNumber);
    @Transactional
    void deleteByAccountLogNumber(Integer accountLogNumber);
    @Query(
            value = "SELECT * FROM account_log " +
                    "WHERE datetime >= ?1 AND datetime <= ?2 AND user_email = ?3"
                    , nativeQuery = true
    )
    List<AccountLogEntity> getAccountLogByDatetime(String startDatetime, String endDatetime,String email);
}
