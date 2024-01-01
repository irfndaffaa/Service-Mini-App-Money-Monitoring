package com.moneymonitoring.moneymonitoring.service;

import java.util.List;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;

public interface SavingService {

    //service category saving
    public SavingCategoryEntity insertCategorySaving(SavingDTO savingDto) throws Exception;
    public SavingCategoryEntity editCategorySaving(SavingDTO savingDto) throws Exception;
    public List<SavingCategoryEntity> getAllCategorySaving();
    public SavingDTO deleteCategorySaving(SavingDTO savingDTO) throws Exception;
    
}
