package com.moneymonitoring.moneymonitoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CATEGORY")
public class CategoryEntity {

    @Id
    private String categoryName;
    private String categoryType;
    
}
