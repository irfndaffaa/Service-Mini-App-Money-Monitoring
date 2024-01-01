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
@Table(name = "SAVING_LOG",schema = "TEST")
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
