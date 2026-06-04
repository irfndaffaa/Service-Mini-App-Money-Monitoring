package com.moneymonitoring.moneymonitoring.repository;

import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<DepositEntity, String> {

    DepositEntity findByIdDeposit(String idDeposit);

    @Query(value = "SELECT MAX(d.ID_DEPOSIT) FROM DEPOSIT d", nativeQuery = true)
    String findTopByIdDeposit();

    @Query(value = """
            SELECT d.SAVING_TYPE,
                   COUNT(*) AS DEPOSIT_COUNT,
                   SUM(CAST(d.DEPOSIT_AMNT AS SIGNED)) AS TOTAL_DEPOSIT,
                   AVG(CAST(d.DEPOSIT_AMNT AS SIGNED)) AS AVG_DEPOSIT
            FROM DEPOSIT d
            WHERE d.TRANSACTION_DATE BETWEEN :startDate AND :endDate
            GROUP BY d.SAVING_TYPE
            ORDER BY TOTAL_DEPOSIT DESC
            """, nativeQuery = true)
    List<Object[]> findDepositSummaryByDateRange(@Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    @Query(value = """
            SELECT d.CAT_EXPENSES,
                   SUM(CAST(d.DEPOSIT_AMNT AS SIGNED)) AS TOTAL_DEPOSIT,
                   RANK() OVER (ORDER BY SUM(CAST(d.DEPOSIT_AMNT AS SIGNED)) DESC) AS RNK
            FROM DEPOSIT d
            GROUP BY d.CAT_EXPENSES
            """, nativeQuery = true)
    List<Object[]> findDepositRankedByCategory();
}
