package com.moneymonitoring.moneymonitoring.repository;

import com.moneymonitoring.moneymonitoring.entity.CategoryBudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CategoryBudgetRepository extends JpaRepository<CategoryBudgetEntity, String> {

    @Query(value = """
            SELECT cb.CATEGORY_NAME,
                   cb.BUDGET_LIMIT,
                   COALESCE(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 0) AS TOTAL_SPENT,
                   (CAST(cb.BUDGET_LIMIT AS SIGNED) - COALESCE(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 0)) AS REMAINING
            FROM CATEGORY_BUDGET cb
            LEFT JOIN WITHDRAW w ON UPPER(w.CAT_EXPENSES) = UPPER(cb.CATEGORY_NAME)
            GROUP BY cb.CATEGORY_NAME, cb.BUDGET_LIMIT
            HAVING (CAST(cb.BUDGET_LIMIT AS SIGNED) - COALESCE(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 0)) < 0
            """, nativeQuery = true)
    List<Object[]> findBudgetsExceeded();

    @Query(value = """
            SELECT cb.CATEGORY_NAME,
                   cb.BUDGET_LIMIT,
                   COALESCE(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 0) AS TOTAL_SPENT,
                   (CAST(cb.BUDGET_LIMIT AS SIGNED) - COALESCE(SUM(CAST(w.WITHDRAWAL_AMNT AS SIGNED)), 0)) AS REMAINING
            FROM CATEGORY_BUDGET cb
            LEFT JOIN WITHDRAW w ON UPPER(w.CAT_EXPENSES) = UPPER(cb.CATEGORY_NAME)
            WHERE cb.CATEGORY_NAME = :categoryName
            GROUP BY cb.CATEGORY_NAME, cb.BUDGET_LIMIT
            """, nativeQuery = true)
    List<Object[]> findBudgetByCategory(@Param("categoryName") String categoryName);
}
