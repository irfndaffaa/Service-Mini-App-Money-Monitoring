package com.moneymonitoring.moneymonitoring.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "WITHDRAW",schema = "TEST")
public class WithdrawEntity {

    @Id
    private String idWithdrawal;
    private String withdrawalAmnt;
    private String catExpenses;
    private String savingType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Jakarta")
    private Date transactionDate;
    
}
