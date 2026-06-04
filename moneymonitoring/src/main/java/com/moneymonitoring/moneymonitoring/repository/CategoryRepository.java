package com.moneymonitoring.moneymonitoring.repository;

import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    List<CategoryEntity> findByCategoryType(String categoryType);

    @Query(value = """
            SELECT c.CATEGORY_TYPE,
                   COUNT(*) AS TOTAL_CATEGORIES,
                   GROUP_CONCAT(c.CATEGORY_NAME ORDER BY c.CATEGORY_NAME SEPARATOR ', ') AS CATEGORY_LIST
            FROM CATEGORY c
            GROUP BY c.CATEGORY_TYPE
            ORDER BY c.CATEGORY_TYPE
            """, nativeQuery = true)
    List<Object[]> findCategorySummary();

    @Query(value = """
            SELECT c.CATEGORY_TYPE,
                   c.CATEGORY_NAME,
                   COALESCE(d.DEPOSIT_COUNT, 0) AS DEPOSIT_COUNT,
                   COALESCE(d.TOTAL_DEPOSIT, 0) AS TOTAL_DEPOSIT,
                   COALESCE(w.WITHDRAWAL_COUNT, 0) AS WITHDRAWAL_COUNT,
                   COALESCE(w.TOTAL_WITHDRAWAL, 0) AS TOTAL_WITHDRAWAL
            FROM CATEGORY c
            LEFT JOIN (
                SELECT sl.TRANSACTION_CAT AS CAT_NAME,
                       COUNT(*) AS DEPOSIT_COUNT,
                       SUM(CAST(sl.TRANSACTION_AMOUNT AS SIGNED)) AS TOTAL_DEPOSIT
                FROM SAVING_LOG sl
                WHERE sl.TRANSACTION_TYPE = 'INCOME'
                GROUP BY sl.TRANSACTION_CAT
            ) d ON UPPER(c.CATEGORY_NAME) = UPPER(d.CAT_NAME)
            LEFT JOIN (
                SELECT sl.TRANSACTION_CAT AS CAT_NAME,
                       COUNT(*) AS WITHDRAWAL_COUNT,
                       SUM(CAST(sl.TRANSACTION_AMOUNT AS SIGNED)) AS TOTAL_WITHDRAWAL
                FROM SAVING_LOG sl
                WHERE sl.TRANSACTION_TYPE = 'EXPENSE'
                GROUP BY sl.TRANSACTION_CAT
            ) w ON UPPER(c.CATEGORY_NAME) = UPPER(w.CAT_NAME)
            WHERE c.CATEGORY_TYPE = :categoryType
            ORDER BY c.CATEGORY_NAME
            """, nativeQuery = true)
    List<Object[]> findCategoryWithTransactionSummary(@Param("categoryType") String categoryType);

    @Query(value = """
            WITH MONTHLY_STATS AS (
                SELECT
                    YEAR(TRANSACTION_DATE) AS TX_YEAR,
                    MONTH(TRANSACTION_DATE) AS TX_MONTH,
                    TRANSACTION_TYPE,
                    SUM(CAST(TRANSACTION_AMOUNT AS SIGNED)) AS TOTAL_AMOUNT,
                    COUNT(*) AS TX_COUNT
                FROM SAVING_LOG
                GROUP BY YEAR(TRANSACTION_DATE), MONTH(TRANSACTION_DATE), TRANSACTION_TYPE
            )
            SELECT
                TX_YEAR,
                TX_MONTH,
                MAX(CASE WHEN TRANSACTION_TYPE = 'INCOME' THEN TOTAL_AMOUNT ELSE 0 END) AS TOTAL_INCOME,
                MAX(CASE WHEN TRANSACTION_TYPE = 'EXPENSE' THEN TOTAL_AMOUNT ELSE 0 END) AS TOTAL_EXPENSE,
                MAX(CASE WHEN TRANSACTION_TYPE = 'INCOME' THEN TOTAL_AMOUNT ELSE 0 END) -
                MAX(CASE WHEN TRANSACTION_TYPE = 'EXPENSE' THEN TOTAL_AMOUNT ELSE 0 END) AS NET_SAVINGS,
                MAX(CASE WHEN TRANSACTION_TYPE = 'INCOME' THEN TX_COUNT ELSE 0 END) AS INCOME_COUNT,
                MAX(CASE WHEN TRANSACTION_TYPE = 'EXPENSE' THEN TX_COUNT ELSE 0 END) AS EXPENSE_COUNT
            FROM MONTHLY_STATS
            GROUP BY TX_YEAR, TX_MONTH
            ORDER BY TX_YEAR DESC, TX_MONTH DESC
            """, nativeQuery = true)
    List<Object[]> findMonthlyFinancialSummary();
}
