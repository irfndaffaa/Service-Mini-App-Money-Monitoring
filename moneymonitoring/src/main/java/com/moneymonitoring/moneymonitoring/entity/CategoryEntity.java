package com.moneymonitoring.moneymonitoring.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "CATEGORY",schema = "TEST")
public class CategoryEntity {

    @Id
    private String categoryName;
    private String categoryType;
    
}
