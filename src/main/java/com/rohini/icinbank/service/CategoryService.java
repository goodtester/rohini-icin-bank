package com.rohini.icinbank.service;

import java.util.List;

import com.rohini.icinbank.domain.common.CategoryInput;
import com.rohini.icinbank.domain.model.Category;
import com.rohini.icinbank.repository.CategoryRepository;
import com.rohini.icinbank.repository.ExpenseDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
@Transactional
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseDetailRepository expenseDetailRepository;

    public List<Category> getCategory() {
        List<Category> categories = categoryRepository.findAll();

        log.info("Get categories success");

        return categories;
    }

    public Category getCategory(Long id) {
        try {
            Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new Exception("Category with id " + id + " not found"));
            log.info("Get category by id success");

            return category;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Category postCategory(CategoryInput category) {
        Category cat = new Category();
        cat.setName(category.getName());

        categoryRepository.save(cat);
        log.info("Category posted");
        return cat;
    }

    public Category updateCategory(Long id, CategoryInput category) {
        Category cat = categoryRepository.getById(id);
        cat.setName(category.getName());

        categoryRepository.save(cat);
        log.info("Category updated");
        return cat; 
    }

    public void deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
            expenseDetailRepository.deleteExpenseDetailByCategory(id);
            log.info("Category deleted");
        } catch (Exception e) {
            log.error("Delete category error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
