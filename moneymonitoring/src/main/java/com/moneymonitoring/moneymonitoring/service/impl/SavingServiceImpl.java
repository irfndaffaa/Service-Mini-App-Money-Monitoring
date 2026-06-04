package com.moneymonitoring.moneymonitoring.service.impl;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.entity.SavingCategoryEntity;
import com.moneymonitoring.moneymonitoring.repository.SavingCategoryRepository;
import com.moneymonitoring.moneymonitoring.service.SavingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SavingServiceImpl implements SavingService {

    private final SavingCategoryRepository savingCategoryRepository;

    @Override
    @CacheEvict(value = "savingCategories", allEntries = true)
    public SavingCategoryEntity insertCategorySaving(SavingDTO savingDto) throws Exception {
        String getId = savingCategoryRepository.findTopByIdSavingCategory();
        int savingId = Optional.ofNullable(getId)
                .map(id -> id.replaceAll("\\D+", ""))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .orElse(0);
        String uid = "SV" + (savingId + 1);

        SavingCategoryEntity data = new SavingCategoryEntity();
        data.setIdCategorySaving(uid);
        data.setSavingName(savingDto.getSavingName());
        data.setMaxOutcome(savingDto.getMaxOutcome());
        return savingCategoryRepository.save(data);
    }

    @Override
    @CacheEvict(value = "savingCategories", allEntries = true)
    public SavingCategoryEntity editCategorySaving(SavingDTO savingDto) throws Exception {
        return Optional.ofNullable(savingCategoryRepository.findByIdCategorySaving(savingDto.getTransactionId()))
                .map(existing -> {
                    existing.setSavingName(savingDto.getSavingName());
                    existing.setMaxOutcome(savingDto.getMaxOutcome());
                    return savingCategoryRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Saving category not found: " + savingDto.getTransactionId()));
    }

    @Override
    @Cacheable(value = "savingCategories", key = "'all'")
    public List<SavingCategoryEntity> getAllCategorySaving() {
        return savingCategoryRepository.findAll();
    }

    @Override
    @CacheEvict(value = "savingCategories", allEntries = true)
    public SavingDTO deleteCategorySaving(SavingDTO savingDTO) throws Exception {
        return Optional.ofNullable(savingCategoryRepository.findByIdCategorySaving(savingDTO.getTransactionId()))
                .map(entity -> {
                    savingCategoryRepository.deleteById(entity.getIdCategorySaving());
                    savingDTO.setMsg("Deleted successfully");
                    return savingDTO;
                })
                .orElseGet(() -> {
                    savingDTO.setMsg("Data not found");
                    return savingDTO;
                });
    }

    public List<String> getSavingNamesAboveThreshold(String threshold) {
        long thresholdVal = Long.parseLong(threshold);
        return savingCategoryRepository.findAll().stream()
                .filter(s -> Long.parseLong(s.getMaxOutcome()) > thresholdVal)
                .map(SavingCategoryEntity::getSavingName)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    public long getTotalMaxOutcome() {
        return savingCategoryRepository.findAll().stream()
                .mapToLong(s -> Long.parseLong(s.getMaxOutcome()))
                .sum();
    }
}
