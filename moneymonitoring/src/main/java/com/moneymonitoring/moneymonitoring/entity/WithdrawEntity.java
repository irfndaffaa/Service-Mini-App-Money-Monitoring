package com.moneymonitoring.moneymonitoring.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WITHDRAW")
public class WithdrawEntity {

    @Id
    private String idWithdrawal;
    private String withdrawalAmnt;
    private String catExpenses;
    private String savingType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Jakarta")
    private Date transactionDate;
    
}
