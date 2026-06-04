package com.moneymonitoring.moneymonitoring.repository;

import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WithdrawalRepository extends JpaRepository<WithdrawEntity, String> {

    WithdrawEntity findByIdWithdrawal(String idWithdrawal);

    @Query(value = "SELECT MAX(w.ID_WITHDRAWAL) FROM WITHDRAW w", nativeQuery = true)
    String findTopByIdWithdrawal();

    @Query(value = """
            SELECT w.CAT_EXPENSES,
                   COUNT(*) AS WITHDRAWAL_COUNT,
                   SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)) AS TOTAL_WITHDRAWAL,
                   AVG(CAST(w.WITHDRAWAL_AMNT AS SIGNED)) AS AVG_WITHDRAWAL
            FROM WITHDRAW w
            WHERE w.TRANSACTION_DATE BETWEEN :startDate AND :endDate
            GROUP BY w.CAT_EXPENSES
            ORDER BY TOTAL_WITHDRAWAL DESC
            """, nativeQuery = true)
    List<Object[]> findWithdrawalSummaryByDateRange(@Param("startDate") Date startDate,
                                                     @Param("endDate") Date endDate);

    @Query(value = """
            SELECT w.SAVING_TYPE,
                   SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)) AS TOTAL_EXPENSE,
                   LAG(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 1, 0) OVER (ORDER BY MIN(w.TRANSACTION_DATE)) AS PREV_EXPENSE,
                   SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)) - LAG(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 1, 0) OVER (ORDER BY MIN(w.TRANSACTION_DATE)) AS EXPENSE_DIFF
            FROM WITHDRAW w
            GROUP BY w.SAVING_TYPE
            """, nativeQuery = true)
    List<Object[]> findWithdrawalWithWindowFunctions();
}
