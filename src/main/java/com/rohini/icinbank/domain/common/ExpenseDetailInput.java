package com.rohini.icinbank.domain.common;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpenseDetailInput {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double amount;
    @Column(nullable = false)
    private Long categoryId;
    @Column(nullable = false)
    private Long expenseId;
}
