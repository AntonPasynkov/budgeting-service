package com.pasynkov.BudgetingService.repository;

import com.pasynkov.BudgetingService.model.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryRepository {
    List<Category> findAll();

    Category findById(int id);

    void save(Category category);

    void update(Category category);

    void delete(Integer categoryId);
}
