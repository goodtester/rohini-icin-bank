package com.rohini.icinbank.domain.common;

import javax.persistence.Column;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpenseInput {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long paymentId;
}
