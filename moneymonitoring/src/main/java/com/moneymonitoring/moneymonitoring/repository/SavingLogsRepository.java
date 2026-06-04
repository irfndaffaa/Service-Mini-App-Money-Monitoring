package com.moneymonitoring.moneymonitoring.repository;

import com.moneymonitoring.moneymonitoring.entity.SavingLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SavingLogsRepository extends JpaRepository<SavingLogsEntity, String> {

    SavingLogsEntity findByIdTransaction(String id);

    @Query(value = "SELECT SUM(CAST(s.TRANSACTION_AMOUNT AS SIGNED)) FROM SAVING_LOG s WHERE s.TRANSACTION_TYPE = :transactionType",
            nativeQuery = true)
    Long findTotalSaving(@Param("transactionType") String transactionType);

    @Query(value = """
            SELECT sl.SAVING_TYPE,
                   sl.TRANSACTION_TYPE,
                   COUNT(*) AS TX_COUNT,
                   SUM(CAST(sl.TRANSACTION_AMOUNT AS SIGNED)) AS TOTAL_AMOUNT
            FROM SAVING_LOG sl
            WHERE sl.TRANSACTION_DATE BETWEEN :startDate AND :endDate
            GROUP BY sl.SAVING_TYPE, sl.TRANSACTION_TYPE
            ORDER BY sl.SAVING_TYPE, sl.TRANSACTION_TYPE
            """, nativeQuery = true)
    List<Object[]> findTransactionSummaryByDateRange(@Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate);

    @Query(value = """
            SELECT sl.SAVING_TYPE,
                   sl.TRANSACTION_CAT,
                   SUM(CAST(sl.TRANSACTION_AMOUNT AS SIGNED)) AS TOTAL_AMOUNT,
                   ROW_NUMBER() OVER (PARTITION BY sl.SAVING_TYPE ORDER BY SUM(CAST(sl.TRANSACTION_AMOUNT AS SIGNED)) DESC) AS RN
            FROM SAVING_LOG sl
            GROUP BY sl.SAVING_TYPE, sl.TRANSACTION_CAT
            """, nativeQuery = true)
    List<Object[]> findTopCategoriesPerSavingType();

    @Query(value = """
            SELECT DISTINCT sl.TRANSACTION_CAT
            FROM SAVING_LOG sl
            WHERE sl.TRANSACTION_TYPE = :transactionType
              AND sl.TRANSACTION_CAT NOT IN (
                  SELECT DISTINCT sl2.TRANSACTION_CAT
                  FROM SAVING_LOG sl2
                  WHERE sl2.TRANSACTION_TYPE = :otherType
              )
            """, nativeQuery = true)
    List<String> findCategoriesOnlyInOneType(@Param("transactionType") String transactionType,
                                              @Param("otherType") String otherType);

    @Query(value = """
            WITH RUNNING_TOTAL AS (
                SELECT
                    sl.ID_TRANSACTION,
                    sl.TRANSACTION_TYPE,
                    CAST(sl.TRANSACTION_AMOUNT AS SIGNED) AS AMOUNT,
                    sl.TRANSACTION_DATE,
                    SUM(CASE WHEN sl.TRANSACTION_TYPE = 'INCOME' THEN CAST(sl.TRANSACTION_AMOUNT AS SIGNED)
                        WHEN sl.TRANSACTION_TYPE = 'EXPENSE' THEN -CAST(sl.TRANSACTION_AMOUNT AS SIGNED)
                        ELSE 0 END) OVER (ORDER BY sl.TRANSACTION_DATE, sl.ID_TRANSACTION) AS RUNNING_BALANCE
                FROM SAVING_LOG sl
            )
            SELECT * FROM RUNNING_TOTAL
            ORDER BY TRANSACTION_DATE, ID_TRANSACTION
            """, nativeQuery = true)
    List<Object[]> findRunningBalance();
}
