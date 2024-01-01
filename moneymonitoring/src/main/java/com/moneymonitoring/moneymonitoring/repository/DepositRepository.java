package com.moneymonitoring.moneymonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;

@Repository
public interface DepositRepository extends JpaRepository<DepositEntity, String> {

    DepositEntity findByIdDeposit(String idDeposit);

    @Query(value = "select max(d.id_deposit) " +
                   "from test.deposit d ", nativeQuery = true)
    String findTopByIdDeposit();
    
}
