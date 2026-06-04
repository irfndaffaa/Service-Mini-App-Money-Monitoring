package com.moneymonitoring.moneymonitoring.service.impl;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.CategoryEntity;
import com.moneymonitoring.moneymonitoring.repository.CategoryRepository;
import com.moneymonitoring.moneymonitoring.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    @Override
    @Cacheable(value = "categories", key = "'allCategories'")
    public List<CategoryEntity> getAllCategory() {
        return categoryRepo.findAll();
    }

    @Override
    @Cacheable(value = "categories", key = "'type:' + #savingDto.categoryType")
    public List<CategoryEntity> getCategoryByType(SavingDTO savingDto) {
        return categoryRepo.findByCategoryType(savingDto.getCategoryType());
    }

    public List<String> getCategoryNamesByType(String type) {
        return categoryRepo.findByCategoryType(type).stream()
                .map(CategoryEntity::getCategoryName)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    public Map<String, List<CategoryEntity>> groupCategoriesByType() {
        return categoryRepo.findAll().stream()
                .collect(Collectors.groupingBy(CategoryEntity::getCategoryType));
    }

    public Map<String, Long> countByType() {
        return categoryRepo.findAll().stream()
                .collect(Collectors.groupingBy(CategoryEntity::getCategoryType, Collectors.counting()));
    }
}
