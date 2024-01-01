package com.moneymonitoring.moneymonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;

@Repository
public interface SavingCategoryRepository extends JpaRepository<SavingCategoryEntity, String> {

    SavingCategoryEntity findByIdCategorySaving(String idCategorySaving);

    SavingCategoryEntity findBySavingName(String savingName);
    
    @Query(value = "select max(sc.id_category_saving) " +
                   "from test.saving_category sc ", nativeQuery = true)
    String findTopByIdSavingCategory();

    
    
}
