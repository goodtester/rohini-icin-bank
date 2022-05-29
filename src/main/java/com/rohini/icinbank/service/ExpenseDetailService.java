package com.rohini.icinbank.service;

import java.util.List;

import com.rohini.icinbank.domain.common.ExpenseDetailInput;
import com.rohini.icinbank.domain.model.Category;
import com.rohini.icinbank.domain.model.Expense;
import com.rohini.icinbank.domain.model.ExpenseDetail;
import com.rohini.icinbank.repository.CategoryRepository;
import com.rohini.icinbank.repository.ExpenseDetailRepository;
import com.rohini.icinbank.repository.ExpenseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
@Transactional
public class ExpenseDetailService {
    @Autowired
    private ExpenseDetailRepository expenseDetailRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    public List<ExpenseDetail> getExpenseDetail(String username) {
        List<ExpenseDetail> expenseDetails = expenseDetailRepository.getExpenseDetailsByUsername(username);

        log.info("Get expense details success");
            
        return expenseDetails;
    }

    public ExpenseDetail getExpenseDetail(String username, Long id) {
        try {
            ExpenseDetail expenseDetail = expenseDetailRepository.getExpenseDetailByUsername(id, username)
                .orElseThrow(() -> new Exception("Expense detail with id " + id + " not found"));
            log.info("Get expense detail by id success");

            return expenseDetail;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public ExpenseDetail postExpenseDetail(String username, ExpenseDetailInput detailInput) {
        try {
            ExpenseDetail exp = new ExpenseDetail();
            exp.setName(detailInput.getName());
            exp.setAmount(detailInput.getAmount());

            Expense expense = expenseRepository.getExpenseByUsername(detailInput.getExpenseId(), username)
                .orElseThrow(() -> new Exception("Expense with id " + detailInput.getExpenseId() + " not found"));

            exp.setExpense(expense);
            
            Category category = categoryRepository.searchById(detailInput.getCategoryId())
                .orElseThrow(() -> new Exception("Category with id " + detailInput.getCategoryId() + " not found"));

            exp.setCategory(category);
            
            expenseDetailRepository.save(exp);
            log.info("Expense detail saved");

            Double totalAmount = expenseRepository.getTotalAmount(detailInput.getExpenseId());
            totalAmount += detailInput.getAmount();
            expenseRepository.updateTotalAmount(detailInput.getExpenseId(), totalAmount);

            return exp; 
        } catch (Exception e) {
            log.error("Save expense detail error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public ExpenseDetail updateExpenseDetail(String username, Long id, ExpenseDetailInput detailInput) {
        try {
            ExpenseDetail exp = expenseDetailRepository.getExpenseDetailByUsername(id, username)
                .orElseThrow(() -> new Exception("Expense detail with id " + id + " not found"));

            if(!username.equals(exp.getExpense().getUser().getUsername())) throw new Exception();

            exp.setName(detailInput.getName());

            Expense expense = expenseRepository.getExpenseByUsername(detailInput.getExpenseId(), username)
                .orElseThrow(() -> new Exception("Expense with id " + detailInput.getExpenseId() + " not found"));

            exp.setExpense(expense);
            
            Double totalAmount = expenseRepository.getTotalAmount(exp.getExpense().getId());
            totalAmount -= exp.getAmount();
            totalAmount += detailInput.getAmount();
            expenseRepository.updateTotalAmount(exp.getExpense().getId(), totalAmount);
            exp.setAmount(detailInput.getAmount());

            Category category = categoryRepository.searchById(detailInput.getCategoryId())
                .orElseThrow(() -> new Exception("Category with id " + detailInput.getCategoryId() + " not found"));

            exp.setCategory(category);
            
            expenseDetailRepository.save(exp);
            log.info("Expense detail updated");

            return exp; 
        } catch (Exception e) {
            log.error("Update expense detail error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteExpenseDetail(String username, Long id) {
        try {
            ExpenseDetail exp = expenseDetailRepository.getExpenseDetailByUsername(id, username)
                .orElseThrow(() -> new Exception("Expense detail with id " + id + " not found"));

            if(!username.equals(exp.getExpense().getUser().getUsername())) throw new Exception();

            expenseDetailRepository.deleteById(id);
            log.info("Expense detail deleted");
        } catch (Exception e) {
            log.error("Delete expense detail error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
