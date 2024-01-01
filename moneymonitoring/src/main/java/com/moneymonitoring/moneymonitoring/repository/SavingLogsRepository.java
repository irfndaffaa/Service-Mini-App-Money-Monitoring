package com.moneymonitoring.moneymonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moneymonitoring.moneymonitoring.entity.SavingLogsEntity;

@Repository
public interface SavingLogsRepository extends JpaRepository<SavingLogsEntity, String> {

    SavingLogsEntity findByIdTransaction(String id);

    @Query(value = "select SUM(s.TRANSACTION_AMOUNT) " +
                   "from test.SAVING_LOG s " +
                   "where s.TRANSACTION_TYPE = ?1", nativeQuery = true)
    int findTotalSaving(String transactionType);
    
}
