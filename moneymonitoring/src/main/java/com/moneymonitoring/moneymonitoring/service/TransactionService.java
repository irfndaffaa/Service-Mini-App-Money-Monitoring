package com.moneymonitoring.moneymonitoring.service;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.dto.TransactionDTO;
import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;

import java.util.List;

public interface TransactionService {
    WithdrawEntity insertWithdrawal(SavingDTO savingDTO) throws Exception;
    WithdrawEntity editWithdrawal(SavingDTO savingDTO) throws Exception;
    DepositEntity insertDeposit(SavingDTO savingDTO) throws Exception;
    DepositEntity editDeposit(SavingDTO savingDTO) throws Exception;
    TransactionDTO getAllTotalSaving();
    List<TransactionDTO> getSavingBreakdown();
}
