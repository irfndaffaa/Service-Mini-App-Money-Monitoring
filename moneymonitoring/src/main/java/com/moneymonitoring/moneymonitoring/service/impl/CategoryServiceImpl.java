package com.moneymonitoring.moneymonitoring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;
import com.moneymonitoring.moneymonitoring.repository.CategoryRepository;
import com.moneymonitoring.moneymonitoring.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepo;


    @Override
    public List<CategoryEntity> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    public List<CategoryEntity> getCategoryByType(SavingDTO savingDto) {

        // List<CategoryEntity> listCategoryByType = new ArrayList<>();

        // try{

        //     List<CategoryEntity> searchCategoryByType = categoryRepo.findByCategoryType(savingDto.getCategoryType());


        // } catch(Exception e){

        //     log.error(e.getMessage(), e);

        // }

        return categoryRepo.findByCategoryType(savingDto.getCategoryType());

        

    }
    
}
