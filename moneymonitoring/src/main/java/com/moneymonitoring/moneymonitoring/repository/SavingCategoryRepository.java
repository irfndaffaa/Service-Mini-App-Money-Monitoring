package com.moneymonitoring.moneymonitoring.repository;

import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingCategoryRepository extends JpaRepository<SavingCategoryEntity, String> {

    SavingCategoryEntity findByIdCategorySaving(String idCategorySaving);

    SavingCategoryEntity findBySavingName(String savingName);

    @Query(value = "SELECT MAX(sc.ID_CATEGORY_SAVING) FROM SAVING_CATEGORY sc", nativeQuery = true)
    String findTopByIdSavingCategory();

    @Query(value = """
            SELECT sc.SAVING_NAME,
                   sc.MAX_OUTCOME,
                   COALESCE(SUM(CAST(sl.TRANSACTION_AMOUNT AS SIGNED)), 0) AS TOTAL_SAVING,
                   (CAST(sc.MAX_OUTCOME AS SIGNED) - COALESCE(SUM(CASE WHEN sl.TRANSACTION_TYPE = 'EXPENSE' THEN CAST(sl.TRANSACTION_AMOUNT AS SIGNED) ELSE 0 END), 0)) AS REMAINING_LIMIT
            FROM SAVING_CATEGORY sc
            LEFT JOIN SAVING_LOG sl ON UPPER(sl.SAVING_TYPE) = UPPER(sc.SAVING_NAME)
            GROUP BY sc.SAVING_NAME, sc.MAX_OUTCOME
            ORDER BY sc.SAVING_NAME
            """, nativeQuery = true)
    List<Object[]> findSavingCategoryWithBalances();

    @Query(value = """
            SELECT sc.SAVING_NAME,
                   sc.MAX_OUTCOME,
                   MAX(sl.TRANSACTION_DATE) AS LAST_TRANSACTION_DATE
            FROM SAVING_CATEGORY sc
            LEFT JOIN SAVING_LOG sl ON UPPER(sl.SAVING_TYPE) = UPPER(sc.SAVING_NAME)
            GROUP BY sc.SAVING_NAME, sc.MAX_OUTCOME
            HAVING MAX(sl.TRANSACTION_DATE) IS NULL
                OR MAX(sl.TRANSACTION_DATE) < :cutoffDate
            """, nativeQuery = true)
    List<Object[]> findInactiveSavingCategories(@Param("cutoffDate") String cutoffDate);
}
