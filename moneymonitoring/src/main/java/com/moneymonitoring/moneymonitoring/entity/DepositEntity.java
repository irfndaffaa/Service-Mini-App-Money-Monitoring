package com.moneymonitoring.moneymonitoring.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "DEPOSIT",schema = "TEST")
public class DepositEntity {
    
    @Id
    private String idDeposit;
    private String depositAmnt;
    private String catExpenses;
    private String savingType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Jakarta")
    private Date transactionDate;
    
}
