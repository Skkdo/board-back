package com.kjh.boardback.repository.account_log;

import com.kjh.boardback.entity.account_log.MoneyCustomTypeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoneyCustomTypeRepository extends JpaRepository<MoneyCustomTypeEntity,Integer> {
    Boolean existsByCustomTypeNumber(Integer customTypeNumber);
    MoneyCustomTypeEntity findByCustomTypeNumber(Integer customTypeNumber);
    @Transactional
    void deleteByCustomTypeNumber(Integer customTypeNumber);
    List<MoneyCustomTypeEntity> findByUserEmail(String email);
    MoneyCustomTypeEntity findByUserEmailAndCustomTypeNumber(String email,int customTypeNumber);
}
