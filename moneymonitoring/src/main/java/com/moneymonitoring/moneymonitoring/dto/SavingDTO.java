package com.moneymonitoring.moneymonitoring.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavingDTO {

    private String transactionId;
    private String transactionAmnt;
    private String withdrawCategory;
    private String depositCategory;
    private String categoryType;
    private String savingName;
    private String maxOutcome;
    private String maxExpenses;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Asia/Jakarta")
    private Date transactionDate;
    private String msg;
    private String totalSaving;


}
