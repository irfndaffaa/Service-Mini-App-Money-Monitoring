package com.moneymonitoring.moneymonitoring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;
import com.moneymonitoring.moneymonitoring.repository.CategoryRepository;
import com.moneymonitoring.moneymonitoring.repository.SavingCategoryRepository;
import com.moneymonitoring.moneymonitoring.service.SavingService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SavingServiceImpl implements SavingService {

    @Autowired
    SavingCategoryRepository savingCategoryRepository;

    @Override
    public SavingCategoryEntity insertCategorySaving(SavingDTO savingDto) throws Exception {
        
        SavingCategoryEntity dataSavingCategory = new SavingCategoryEntity();
        
        int savingId = 1;

        try{

            String getId = savingCategoryRepository.findTopByIdSavingCategory();
            savingId = (getId == null) ? 0 : Integer.parseInt(getId.replaceAll("\\D+",""));
            String uid = "SV" + (savingId + 1);

            dataSavingCategory.setIdCategorySaving(uid);
            dataSavingCategory.setSavingName(savingDto.getSavingName());
            dataSavingCategory.setMaxOutcome(savingDto.getMaxOutcome());

            savingCategoryRepository.save(dataSavingCategory);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return dataSavingCategory;
    }

    @Override
    public SavingCategoryEntity editCategorySaving(SavingDTO savingDto) throws Exception {

        SavingCategoryEntity newDataSavingCategory = new SavingCategoryEntity();

        try{

            SavingCategoryEntity dataSavingCategory = savingCategoryRepository.findByIdCategorySaving(savingDto.getTransactionId());
            newDataSavingCategory.setIdCategorySaving(dataSavingCategory.getIdCategorySaving());
            newDataSavingCategory.setSavingName(savingDto.getSavingName());
            newDataSavingCategory.setMaxOutcome(savingDto.getMaxOutcome());

            savingCategoryRepository.save(newDataSavingCategory);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return newDataSavingCategory;
        
    }

    @Override
    public List<SavingCategoryEntity> getAllCategorySaving() {
        return savingCategoryRepository.findAll();
    }

    @Override
    public SavingDTO deleteCategorySaving(SavingDTO savingDTO) throws Exception {

        String msg = "";

        try{

            SavingCategoryEntity dataSavingCategory = savingCategoryRepository.findByIdCategorySaving(savingDTO.getTransactionId());

            if(dataSavingCategory == null){
                msg = "Data not found";
                savingDTO.setMsg(msg);
            } else {
                savingCategoryRepository.deleteById(dataSavingCategory.getIdCategorySaving());
                msg = "Deleted successfully";
                savingDTO.setMsg(msg);
            }


        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return savingDTO;
    }

    

}
