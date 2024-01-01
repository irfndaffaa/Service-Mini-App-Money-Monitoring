package com.moneymonitoring.moneymonitoring.service;

import com.moneymonitoring.moneymonitoring.dto.SavingDTO;
import com.moneymonitoring.moneymonitoring.dto.TransactionDTO;
import com.moneymonitoring.moneymonitoring.entity.DepositEntity;
import com.moneymonitoring.moneymonitoring.entity.WithdrawEntity;

public interface TransactionService {

    //service withdrawal
    public WithdrawEntity insertWithdrawal(SavingDTO savingDTO) throws Exception;
    public WithdrawEntity editWithdrawal(SavingDTO savingDTO) throws Exception;

    //service deposit
    public DepositEntity insertDeposit(SavingDTO savingDTO) throws Exception;
    public DepositEntity editDeposit(SavingDTO savingDTO) throws Exception;

    //service get total saving
    public TransactionDTO getAllTotalSaving();
    
}
