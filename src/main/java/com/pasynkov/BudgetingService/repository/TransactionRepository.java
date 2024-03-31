package com.pasynkov.BudgetingService.repository;

import com.pasynkov.BudgetingService.model.Category;
import com.pasynkov.BudgetingService.model.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionRepository {
    List<Transaction> findAll();

    Transaction findById(int id);

    List<Transaction> findTransactionsByCategoryId(int categoryId);

    List<Transaction> findTransactionsByType(String type);

    void save(Transaction category);

    void update(Transaction category);

    void delete(Integer categoryId);
}
