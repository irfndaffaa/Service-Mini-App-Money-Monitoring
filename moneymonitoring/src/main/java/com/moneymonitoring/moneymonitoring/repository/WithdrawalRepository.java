package com.moneymonitoring.moneymonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;

@Repository
public interface WithdrawalRepository extends JpaRepository<WithdrawEntity, String> {

    WithdrawEntity findByIdWithdrawal(String idWithdrawal);

    @Query(value = "select max(w.id_withdrawal) " +
                   "from test.withdraw w ", nativeQuery = true)
    String findTopByIdWithdrawal();
    
}
