package com.moneymonitoring.moneymonitoring.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.dto.TransactionDTO;
import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import com.moneymonitoring.moneymonitoring.entity.SavingLogsEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;
import com.moneymonitoring.moneymonitoring.repository.DepositRepository;
import com.moneymonitoring.moneymonitoring.repository.SavingLogsRepository;
import com.moneymonitoring.moneymonitoring.repository.WithdrawalRepository;
import com.moneymonitoring.moneymonitoring.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    WithdrawalRepository withdrawalRepo;
    @Autowired
    SavingLogsRepository savingLogRepo;
    @Autowired
    DepositRepository depositRepo;


    @Override
    public WithdrawEntity insertWithdrawal(SavingDTO savingDTO) throws Exception {

        WithdrawEntity dataWithdraw = new WithdrawEntity();
        SavingLogsEntity dataSavingLog = new SavingLogsEntity();
        int withdrawId = 1;

        try{

            String getMaxWithdrawId = withdrawalRepo.findTopByIdWithdrawal();
            withdrawId = (getMaxWithdrawId == null) ? 0 : Integer.parseInt(getMaxWithdrawId.replaceAll("\\D+",""));
            String uid = "WITHDRAW" + (withdrawId + 1);

            //insert to table withdrawal
            dataWithdraw.setIdWithdrawal(uid);
            dataWithdraw.setWithdrawalAmnt(savingDTO.getTransactionAmnt());
            dataWithdraw.setCatExpenses(savingDTO.getWithdrawCategory().toUpperCase());
            dataWithdraw.setSavingType(savingDTO.getCategoryType().toUpperCase());
            dataWithdraw.setTransactionDate(new Date());

            withdrawalRepo.save(dataWithdraw);

            //insert saving log data
            dataSavingLog.setIdTransaction(uid);
            dataSavingLog.setTransactionAmount(savingDTO.getTransactionAmnt());
            dataSavingLog.setTransactionType(savingDTO.getCategoryType().toUpperCase());
            dataSavingLog.setTransactionCat(savingDTO.getWithdrawCategory().toUpperCase());
            dataSavingLog.setTransactionDate(new Date());
            dataSavingLog.setSavingType(savingDTO.getSavingName().toUpperCase());

            savingLogRepo.save(dataSavingLog);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return dataWithdraw;
        
    }

    @Override
    public WithdrawEntity editWithdrawal(SavingDTO savingDTO) throws Exception {
        
        WithdrawEntity dataWithdraw = withdrawalRepo.findByIdWithdrawal(savingDTO.getTransactionId());
        SavingLogsEntity dataSavingLog = savingLogRepo.findByIdTransaction(savingDTO.getTransactionId());


        try{

            dataWithdraw.setIdWithdrawal(dataWithdraw.getIdWithdrawal());
            dataWithdraw.setWithdrawalAmnt(savingDTO.getTransactionAmnt());
            dataWithdraw.setCatExpenses(savingDTO.getWithdrawCategory().toUpperCase());
            dataWithdraw.setSavingType(savingDTO.getCategoryType().toUpperCase());
            dataWithdraw.setTransactionDate(dataWithdraw.getTransactionDate());

            withdrawalRepo.save(dataWithdraw);

            dataSavingLog.setIdTransaction(dataSavingLog.getIdTransaction());
            dataSavingLog.setSavingType(savingDTO.getSavingName().toUpperCase());
            dataSavingLog.setTransactionAmount(dataWithdraw.getWithdrawalAmnt());
            dataSavingLog.setTransactionCat(dataWithdraw.getCatExpenses().toUpperCase());
            dataSavingLog.setTransactionType(savingDTO.getCategoryType().toUpperCase());
            dataSavingLog.setTransactionDate(dataSavingLog.getTransactionDate());

            savingLogRepo.save(dataSavingLog);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return dataWithdraw;

    }

    @Override
    public DepositEntity insertDeposit(SavingDTO savingDTO) throws Exception {
        
        DepositEntity dataDeposit = new DepositEntity();
        SavingLogsEntity dataSavingLog = new SavingLogsEntity();
        int depositId = 1;

        try{

            String getMaxDepositId = depositRepo.findTopByIdDeposit();
            depositId = (getMaxDepositId == null) ? 0 : Integer.parseInt(getMaxDepositId.replaceAll("\\D+",""));
            String uid = "DEPOSIT" + (depositId + 1);

            //insert to table withdrawal
            dataDeposit.setIdDeposit(uid);
            dataDeposit.setDepositAmnt(savingDTO.getTransactionAmnt().toUpperCase());
            dataDeposit.setCatExpenses(savingDTO.getDepositCategory().toUpperCase());
            dataDeposit.setSavingType(savingDTO.getCategoryType().toUpperCase());
            dataDeposit.setTransactionDate(new Date());

            depositRepo.save(dataDeposit);

            //insert saving log data
            dataSavingLog.setIdTransaction(uid);
            dataSavingLog.setTransactionAmount(savingDTO.getTransactionAmnt());
            dataSavingLog.setTransactionType(savingDTO.getCategoryType());
            dataSavingLog.setTransactionCat(savingDTO.getDepositCategory().toUpperCase());
            dataSavingLog.setTransactionDate(new Date());
            dataSavingLog.setSavingType(savingDTO.getSavingName().toUpperCase());

            savingLogRepo.save(dataSavingLog);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return dataDeposit;

    }

    @Override
    public DepositEntity editDeposit(SavingDTO savingDTO) throws Exception {

        DepositEntity dataDeposit = depositRepo.findByIdDeposit(savingDTO.getTransactionId());
        SavingLogsEntity dataSavingLog = savingLogRepo.findByIdTransaction(savingDTO.getTransactionId());


        try{

            dataDeposit.setIdDeposit(dataDeposit.getIdDeposit());
            dataDeposit.setDepositAmnt(savingDTO.getTransactionAmnt());
            dataDeposit.setCatExpenses(savingDTO.getDepositCategory().toUpperCase());
            dataDeposit.setSavingType(savingDTO.getCategoryType().toUpperCase());
            dataDeposit.setTransactionDate(dataDeposit.getTransactionDate());

            depositRepo.save(dataDeposit);

            dataSavingLog.setIdTransaction(dataSavingLog.getIdTransaction());
            dataSavingLog.setSavingType(savingDTO.getSavingName().toUpperCase());
            dataSavingLog.setTransactionAmount(dataDeposit.getDepositAmnt());
            dataSavingLog.setTransactionCat(dataDeposit.getCatExpenses().toUpperCase());
            dataSavingLog.setTransactionType(savingDTO.getCategoryType().toUpperCase());
            dataSavingLog.setTransactionDate(dataSavingLog.getTransactionDate());

            savingLogRepo.save(dataSavingLog);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return dataDeposit;
        
    }

    @Override
    public TransactionDTO getAllTotalSaving() {
        
        TransactionDTO dataTransaction = new TransactionDTO();

        long totalIncome = savingLogRepo.findTotalSaving("INCOME");
        long totalExpense = savingLogRepo.findTotalSaving("EXPENSE");

        String totalSaving = Long.toString(totalIncome-totalExpense);

        try{

            dataTransaction.setTotalSaving(totalSaving);

        } catch(Exception e){

            log.error(e.getMessage(), e);

        }

        return dataTransaction;

    }
    
}
