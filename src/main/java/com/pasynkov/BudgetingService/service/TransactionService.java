package com.pasynkov.BudgetingService.service;

import com.pasynkov.BudgetingService.model.Category;
import com.pasynkov.BudgetingService.model.Transaction;
import com.pasynkov.BudgetingService.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public void save(Transaction transaction) {
        repository.save(transaction);
    }

    public void update(Transaction transaction) {
        repository.update(transaction);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public Transaction findById(int id) {
        return repository.findById(id);
    }

    public List<Transaction> findTransactionsByCategoryId(int categoryId) {
        return repository.findTransactionsByCategoryId(categoryId);
    }

    public BigDecimal calculateTotalBudget() {
        List<Transaction> transactions = this.findAll();
        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> "income".equalsIgnoreCase(transaction.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = transactions.stream()
                .filter(transaction -> "expense".equalsIgnoreCase(transaction.getType()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpense);
    }

    public List<Transaction> findTransactionsByType(String type) {
        return repository.findTransactionsByType(type);
    }
}
