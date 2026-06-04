package com.moneymonitoring.moneymonitoring.service.impl;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.dto.TransactionDTO;
import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import com.moneymonitoring.moneymonitoring.entity.SavingLogsEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;
import com.moneymonitoring.moneymonitoring.kafka.TransactionEvent;
import com.moneymonitoring.moneymonitoring.kafka.TransactionEventProducer;
import com.moneymonitoring.moneymonitoring.repository.DepositRepository;
import com.moneymonitoring.moneymonitoring.repository.SavingLogsRepository;
import com.moneymonitoring.moneymonitoring.repository.WithdrawalRepository;
import com.moneymonitoring.moneymonitoring.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {

    private final WithdrawalRepository withdrawalRepo;
    private final SavingLogsRepository savingLogRepo;
    private final DepositRepository depositRepo;
    private final TransactionEventProducer eventProducer;

    public TransactionServiceImpl(
            WithdrawalRepository withdrawalRepo,
            SavingLogsRepository savingLogRepo,
            DepositRepository depositRepo,
            TransactionEventProducer eventProducer) {
        this.withdrawalRepo = withdrawalRepo;
        this.savingLogRepo = savingLogRepo;
        this.depositRepo = depositRepo;
        this.eventProducer = eventProducer;
    }

    @Override
    @CacheEvict(value = "totalSaving", allEntries = true)
    public WithdrawEntity insertWithdrawal(SavingDTO savingDTO) throws Exception {
        String uid = generateWithdrawalId();
        WithdrawEntity dataWithdraw = buildWithdrawEntity(uid, savingDTO);
        withdrawalRepo.save(dataWithdraw);
        SavingLogsEntity dataSavingLog = buildSavingLog(uid, savingDTO.getWithdrawCategory(), savingDTO);
        savingLogRepo.save(dataSavingLog);
        publishTransactionEvent("WITHDRAWAL", uid, savingDTO);
        return dataWithdraw;
    }

    @Override
    @CacheEvict(value = "totalSaving", allEntries = true)
    public WithdrawEntity editWithdrawal(SavingDTO savingDTO) throws Exception {
        WithdrawEntity dataWithdraw = withdrawalRepo.findByIdWithdrawal(savingDTO.getTransactionId());
        SavingLogsEntity dataSavingLog = savingLogRepo.findByIdTransaction(savingDTO.getTransactionId());
        dataWithdraw.setWithdrawalAmnt(savingDTO.getTransactionAmnt());
        dataWithdraw.setCatExpenses(savingDTO.getWithdrawCategory().toUpperCase());
        dataWithdraw.setSavingType(savingDTO.getCategoryType().toUpperCase());
        withdrawalRepo.save(dataWithdraw);
        dataSavingLog.setSavingType(savingDTO.getSavingName().toUpperCase());
        dataSavingLog.setTransactionAmount(dataWithdraw.getWithdrawalAmnt());
        dataSavingLog.setTransactionCat(dataWithdraw.getCatExpenses().toUpperCase());
        dataSavingLog.setTransactionType(savingDTO.getCategoryType().toUpperCase());
        savingLogRepo.save(dataSavingLog);
        publishTransactionEvent("EDIT_WITHDRAWAL", dataWithdraw.getIdWithdrawal(), savingDTO);
        return dataWithdraw;
    }

    @Override
    @CacheEvict(value = "totalSaving", allEntries = true)
    public DepositEntity insertDeposit(SavingDTO savingDTO) throws Exception {
        String uid = generateDepositId();
        DepositEntity dataDeposit = buildDepositEntity(uid, savingDTO);
        depositRepo.save(dataDeposit);
        SavingLogsEntity dataSavingLog = buildSavingLog(uid, savingDTO.getDepositCategory(), savingDTO);
        savingLogRepo.save(dataSavingLog);
        publishTransactionEvent("DEPOSIT", uid, savingDTO);
        return dataDeposit;
    }

    @Override
    @CacheEvict(value = "totalSaving", allEntries = true)
    public DepositEntity editDeposit(SavingDTO savingDTO) throws Exception {
        DepositEntity dataDeposit = depositRepo.findByIdDeposit(savingDTO.getTransactionId());
        SavingLogsEntity dataSavingLog = savingLogRepo.findByIdTransaction(savingDTO.getTransactionId());
        dataDeposit.setDepositAmnt(savingDTO.getTransactionAmnt());
        dataDeposit.setCatExpenses(savingDTO.getDepositCategory().toUpperCase());
        dataDeposit.setSavingType(savingDTO.getCategoryType().toUpperCase());
        depositRepo.save(dataDeposit);
        dataSavingLog.setSavingType(savingDTO.getSavingName().toUpperCase());
        dataSavingLog.setTransactionAmount(dataDeposit.getDepositAmnt());
        dataSavingLog.setTransactionCat(dataDeposit.getCatExpenses().toUpperCase());
        dataSavingLog.setTransactionType(savingDTO.getCategoryType().toUpperCase());
        savingLogRepo.save(dataSavingLog);
        publishTransactionEvent("EDIT_DEPOSIT", dataDeposit.getIdDeposit(), savingDTO);
        return dataDeposit;
    }

    @Override
    @Cacheable(value = "totalSaving", key = "'savingTotal'")
    public TransactionDTO getAllTotalSaving() {
        long totalIncome = Optional.ofNullable(savingLogRepo.findTotalSaving("INCOME")).orElse(0L);
        long totalExpense = Optional.ofNullable(savingLogRepo.findTotalSaving("EXPENSE")).orElse(0L);
        TransactionDTO dto = new TransactionDTO();
        dto.setTotalSaving(Long.toString(totalIncome - totalExpense));
        return dto;
    }

    @Override
    public List<TransactionDTO> getSavingBreakdown() {
        return savingLogRepo.findAll().stream()
                .filter(s -> s.getTransactionAmount() != null)
                .collect(Collectors.groupingBy(
                        SavingLogsEntity::getTransactionType,
                        Collectors.summingLong(s -> Long.parseLong(s.getTransactionAmount()))
                ))
                .entrySet().stream()
                .map(entry -> {
                    TransactionDTO dto = new TransactionDTO();
                    dto.setTotalSaving(entry.getKey() + ":" + entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private String generateWithdrawalId() {
        String maxId = withdrawalRepo.findTopByIdWithdrawal();
        int nextId = Optional.ofNullable(maxId)
                .map(id -> id.replaceAll("\\D+", ""))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .orElse(0) + 1;
        return "WITHDRAW" + nextId;
    }

    private String generateDepositId() {
        String maxId = depositRepo.findTopByIdDeposit();
        int nextId = Optional.ofNullable(maxId)
                .map(id -> id.replaceAll("\\D+", ""))
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .orElse(0) + 1;
        return "DEPOSIT" + nextId;
    }

    private WithdrawEntity buildWithdrawEntity(String uid, SavingDTO dto) {
        WithdrawEntity e = new WithdrawEntity();
        e.setIdWithdrawal(uid);
        e.setWithdrawalAmnt(dto.getTransactionAmnt());
        e.setCatExpenses(dto.getWithdrawCategory().toUpperCase());
        e.setSavingType(dto.getCategoryType().toUpperCase());
        e.setTransactionDate(new Date());
        return e;
    }

    private DepositEntity buildDepositEntity(String uid, SavingDTO dto) {
        DepositEntity e = new DepositEntity();
        e.setIdDeposit(uid);
        e.setDepositAmnt(dto.getTransactionAmnt());
        e.setCatExpenses(dto.getDepositCategory().toUpperCase());
        e.setSavingType(dto.getCategoryType().toUpperCase());
        e.setTransactionDate(new Date());
        return e;
    }

    private SavingLogsEntity buildSavingLog(String uid, String category, SavingDTO dto) {
        SavingLogsEntity e = new SavingLogsEntity();
        e.setIdTransaction(uid);
        e.setTransactionAmount(dto.getTransactionAmnt());
        e.setTransactionType(dto.getCategoryType().toUpperCase());
        e.setTransactionCat(category.toUpperCase());
        e.setTransactionDate(new Date());
        e.setSavingType(dto.getSavingName().toUpperCase());
        return e;
    }

    private void publishTransactionEvent(String eventType, String txId, SavingDTO dto) {
        try {
            TransactionEvent event = new TransactionEvent(
                    UUID.randomUUID().toString(),
                    eventType,
                    txId,
                    dto.getCategoryType(),
                    dto.getTransactionAmnt(),
                    dto.getWithdrawCategory() != null ? dto.getWithdrawCategory() : dto.getDepositCategory(),
                    new Date(),
                    dto.getSavingName(),
                    "COMPLETED"
            );
            eventProducer.sendTransactionEvent(event);
        } catch (Exception e) {
            log.warn("Failed to publish Kafka event: {}", e.getMessage());
        }
    }
}
