package com.moneymonitoring.moneymonitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "CATEGORY_BUDGET")
public class CategoryBudgetEntity {

    @Id
    private String id;
    private String categoryName;
    private String budgetLimit;
    private Date createdDate;
}
