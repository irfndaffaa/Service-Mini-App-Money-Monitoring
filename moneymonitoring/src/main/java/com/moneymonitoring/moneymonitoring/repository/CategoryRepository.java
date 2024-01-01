package com.moneymonitoring.moneymonitoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;
import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    List<CategoryEntity> findByCategoryType(String categoryType);
    
}
