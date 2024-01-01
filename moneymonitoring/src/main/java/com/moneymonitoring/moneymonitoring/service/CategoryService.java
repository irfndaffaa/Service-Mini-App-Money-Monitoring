package com.moneymonitoring.moneymonitoring.service;

import java.util.List;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;

public interface CategoryService {

    //service category
    public List<CategoryEntity> getAllCategory();
    public List<CategoryEntity> getCategoryByType(SavingDTO savingDto);
    
}
