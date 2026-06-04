package com.moneymonitoring.moneymonitoring.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SAVING_LOG")
public class SavingLogsEntity {
    
    @Id
    private String idTransaction;
    private String transactionType;
    private String transactionAmount;
    private String transactionCat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Jakarta")
    private Date transactionDate;
    private String savingType;

}
