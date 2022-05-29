package com.rohini.icinbank.service;

import java.time.LocalDateTime;
import java.util.List;

import com.rohini.icinbank.domain.common.ExpenseInput;
import com.rohini.icinbank.domain.model.Expense;
import com.rohini.icinbank.domain.model.Payment;
import com.rohini.icinbank.domain.model.User;
import com.rohini.icinbank.repository.ExpenseDetailRepository;
import com.rohini.icinbank.repository.ExpenseRepository;
import com.rohini.icinbank.repository.PaymentRepository;
import com.rohini.icinbank.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@NoArgsConstructor
@Transactional
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ExpenseDetailRepository expenseDetailRepository;

    public List<Expense> getExpense(String username) {
        List<Expense> expense = expenseRepository.getExpensesByUsername(username);

        log.info("Get expenses success");

        return expense;
    }

    public Expense getExpense(String username, Long id) {
        try {
            Expense expense = expenseRepository.getExpenseByUsername(id, username)
                .orElseThrow(() -> new Exception("Expense with id " + id + " not found"));
            log.info("Get expense by id success");            

            return expense;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Expense postExpense(String username, ExpenseInput expense) {
        try {
            Expense exp = new Expense();

            exp.setName(expense.getName());
            exp.setExpenseDate(LocalDateTime.now());  

            User user = userRepository.findByUsername(username);

            exp.setUser(user);

            Payment payment = paymentRepository.searchById(expense.getPaymentId())
                .orElseThrow(() -> new Exception("Payment with id " + expense.getPaymentId() + " not found"));

            exp.setPayment(payment);
            
            expenseRepository.save(exp);
            log.info("Expense saved");

            return exp; 
        } catch (Exception e) {
            log.error("Save expense error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Expense updateExpense(String username, Long id, ExpenseInput expense) {
        try {
            Expense exp = expenseRepository.getExpenseByUsername(id, username)
                .orElseThrow(() -> new Exception("Expense with id " + id + " not found"));

            if(!username.equals(exp.getUser().getUsername())) throw new Exception();

            exp.setName(expense.getName());

            Payment payment = paymentRepository.searchById(expense.getPaymentId())
                .orElseThrow(() -> new Exception("Payment with id " + expense.getPaymentId() + " not found"));

            exp.setPayment(payment);

            expenseRepository.save(exp);
            log.info("Expense updated");

            return exp; 
        } catch (Exception e) {
            log.error("Update expense error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteExpense(String username, Long id) {
        try {
            Expense exp = expenseRepository.getExpenseByUsername(id, username)
                .orElseThrow(() -> new Exception("Expense with id " + id + " not found"));

            if(!username.equals(exp.getUser().getUsername())) throw new Exception();

            expenseRepository.deleteById(id);
            expenseDetailRepository.deleteExpenseDetailByExpense(id);
            log.info("Expense deleted");
        } catch (Exception e) {
            log.error("Delete expense error: {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
