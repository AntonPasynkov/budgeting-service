package com.pasynkov.BudgetingService.service;

import com.pasynkov.BudgetingService.model.Category;
import com.pasynkov.BudgetingService.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public void save(Category category) {
        repository.save(category);
    }

    public void update(Category category) {
        repository.update(category);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public List<Category> findAll() {
        return repository.findAll();
    }

    public Category findById(int id) {
        return repository.findById(id);
    }
}
