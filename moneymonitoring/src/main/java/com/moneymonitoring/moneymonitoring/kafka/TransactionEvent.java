package com.moneymonitoring.moneymonitoring.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String eventId;
    private String eventType;
    private String transactionId;
    private String transactionType;
    private String amount;
    private String category;
    private Date transactionDate;
    private String savingType;
    private String status;
}
