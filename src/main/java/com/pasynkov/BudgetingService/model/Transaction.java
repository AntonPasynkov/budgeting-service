package com.pasynkov.BudgetingService.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("Transaction")
public class Transaction {
    private Integer id;

    @Valid
    private Category category;

    @Min(value = 0, message = "Сумма транзакции должна быть больше 0")
    private BigDecimal amount;

    @NotNull(message = "Дата транзакции не должна быть пустой")
    private LocalDate transactionDate;

    @NotEmpty(message = "Тип транзакции не должен быть пустой")
    private String type;

    private String description;
}
