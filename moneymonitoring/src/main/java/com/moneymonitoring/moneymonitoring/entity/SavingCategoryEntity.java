package com.moneymonitoring.moneymonitoring.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SAVING_CATEGORY",schema = "TEST")
public class SavingCategoryEntity {

    @Id
    private String idCategorySaving;
    private String savingName;
    private String maxOutcome;
    
}
