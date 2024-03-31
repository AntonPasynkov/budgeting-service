package com.pasynkov.BudgetingService.controller;

import com.pasynkov.BudgetingService.model.Category;
import com.pasynkov.BudgetingService.model.Transaction;
import com.pasynkov.BudgetingService.service.CategoryService;
import com.pasynkov.BudgetingService.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class BudgetController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;

    public BudgetController(TransactionService transactionService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/transactions")
    public String showBudgetOverview(Model model, Locale locale) {
        model.addAttribute("locale", locale);
        model.addAttribute("totalBudget", transactionService.calculateTotalBudget());
        model.addAttribute("income", transactionService.findTransactionsByType("income"));
        model.addAttribute("expense", transactionService.findTransactionsByType("expense"));

        return "transactions";
    }

    @GetMapping("/transactions/income/new")
    public String showAddIncomeForm(Model model) {
        Transaction transaction = new Transaction();
        transaction.setType("income");
        transaction.setCategory(categoryService.findById(1));
        model.addAttribute("income", transaction);

        return "addIncome";
    }

    @PostMapping("/transactions/income")
    public String processAddIncome(@ModelAttribute @Valid Transaction income, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addIncome";
        }
        income.setCategory(categoryService.findById(1));
        transactionService.save(income);

        return "redirect:/transactions";
    }

    @GetMapping("/transactions/expense/new")
    public String showAddExpenseForm(Model model) {
        List<Category> categories = categoryService.findAll()
                .stream()
                .filter(category -> !category.getId().equals(1))
                .collect(Collectors.toList());
        Transaction transaction = new Transaction();
        transaction.setType("expense");
        model.addAttribute("categories", categories);
        model.addAttribute("expense", transaction);

        return "addExpense";
    }

    @PostMapping("/transactions/expense")
    public String processAddExpense(@ModelAttribute @Valid Transaction expense, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() && expense.getCategory().getName() != null) {
            model.addAttribute("categories", categoryService.findAll()
                    .stream()
                    .filter(category -> !category.getId().equals(1))
                    .collect(Collectors.toList()));
            model.addAttribute("expense", expense);
            return "addExpense";
        }
        Category selectedCategory = categoryService.findById(expense.getCategory().getId());
        expense.setCategory(selectedCategory);
        transactionService.save(expense);

        return "redirect:/transactions";
    }

    @GetMapping("/transactions/income/{id}/edit")
    public String showEditIncomeForm(@PathVariable("id") int id, Model model) {
        Transaction transaction = transactionService.findById(id);
        Date date = java.sql.Date.valueOf(transaction.getTransactionDate());
        model.addAttribute("income", transaction);
        model.addAttribute("formattedDate", date);

        return "editIncome";
    }

    @PatchMapping("/transactions/income/{id}")
    public String processEditIncome(@ModelAttribute @Valid Transaction income, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editIncome";
        }
        income.setCategory(categoryService.findById(1));
        transactionService.update(income);

        return "redirect:/transactions";
    }

    @DeleteMapping("/transactions/income/{id}")
    public String deleteIncome(@PathVariable("id") int id) {
        transactionService.delete(id);

        return "redirect:/transactions";
    }

    @GetMapping("/transactions/expense/{id}/edit")
    public String showEditExpenseForm(@PathVariable("id") int id, Model model) {
        Transaction transaction = transactionService.findById(id);
        List<Category> categories = categoryService.findAll()
                .stream()
                .filter(category -> !category.getId().equals(1))
                .collect(Collectors.toList());
        Date date = java.sql.Date.valueOf(transaction.getTransactionDate());
        model.addAttribute("expense", transaction);
        model.addAttribute("formattedDate", date);
        model.addAttribute("categories", categories);

        return "editExpense";
    }

    @PatchMapping("/transactions/expense/{id}")
    public String processEditExpense(@ModelAttribute @Valid Transaction expense, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors() && expense.getCategory().getName() != null) {
            model.addAttribute("categories", categoryService.findAll()
                    .stream()
                    .filter(category -> !category.getId().equals(1))
                    .collect(Collectors.toList()));
            Date date = java.sql.Date.valueOf(expense.getTransactionDate());
            model.addAttribute("expense", expense);
            model.addAttribute("formattedDate", date);
            return "editExpense";
        }
        Category selectedCategory = categoryService.findById(expense.getCategory().getId());
        expense.setCategory(selectedCategory);
        transactionService.update(expense);

        return "redirect:/transactions";
    }

    @DeleteMapping("/transactions/expense/{id}")
    public String deleteExpense(@PathVariable("id") int id) {
        transactionService.delete(id);

        return "redirect:/transactions";
    }

    @GetMapping("/transactions/categories")
    public String showCategories(Model model) {
        List<Category> categories = categoryService.findAll()
                .stream()
                .filter(category -> !category.getId().equals(1))
                .collect(Collectors.toList());
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/transactions/category/new")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory";
    }

    @PostMapping("/transactions/category")
    public String processAddCategory(@ModelAttribute @Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addCategory";
        }
        categoryService.save(category);
        return "redirect:/transactions/categories";
    }

    @GetMapping("/transactions/category/{id}/edit")
    public String showEditCategoryForm(@PathVariable("id") int id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "editCategory";
    }

    @PatchMapping("/transactions/category/{id}")
    public String processEditCategory(@ModelAttribute @Valid Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editCategory";
        }
        categoryService.update(category);
        return "redirect:/transactions/categories";
    }

    @DeleteMapping("/transactions/category/{id}")
    public String processDeleteCategory(@PathVariable("id") int id, Model model) {
        List<Transaction> transactions = transactionService.findTransactionsByCategoryId(id);
        if (transactions.isEmpty()) {
            categoryService.delete(id);
            return "redirect:/transactions/categories";
        } else {
            Category category = categoryService.findById(id);
            model.addAttribute("category", category);
            model.addAttribute("error", "Категория не может быть удалена, так как она используется в транзакциях.");
            return "editCategory";
        }
    }
}
