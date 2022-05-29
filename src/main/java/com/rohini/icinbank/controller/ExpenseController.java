package com.rohini.icinbank.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rohini.icinbank.domain.common.ExpenseInput;
import com.rohini.icinbank.domain.model.Expense;
import com.rohini.icinbank.security.JwtTokenProvider;
import com.rohini.icinbank.service.ExpenseService;
import com.rohini.icinbank.util.ResponseUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    private String username;
    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/expense")
    public ResponseEntity<?> getExpense(HttpServletRequest request) {
        try {
            username = getUsername(request);
            List<Expense> expense = expenseService.getExpense(username);
            return ResponseUtil.build("Get all expense", expense, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/expense/{id}")
    public ResponseEntity<?> getExpense(HttpServletRequest request, @PathVariable("id") Long id) {
        try {
            username = getUsername(request);
            Expense expense = expenseService.getExpense(username, id);
            return ResponseUtil.build("Get expense by id", expense, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/expense")
    public ResponseEntity<?> postExpense (HttpServletRequest request, @RequestBody ExpenseInput req) {
        try {
            username = getUsername(request);
            Expense expense = expenseService.postExpense(username, req);
            return ResponseUtil.build("Expense saved", expense, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/expense/{id}")
    public ResponseEntity<?> putExpense(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody ExpenseInput req) {
        try {
            username = getUsername(request);
            Expense expense = expenseService.updateExpense(username, id, req);
            return ResponseUtil.build("Expense updated", expense, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/expense/{id}")
    public ResponseEntity<?> deleteExpense(HttpServletRequest request, @PathVariable("id") Long id) {
        try {
            username = getUsername(request);
            expenseService.deleteExpense(username, id);
            return ResponseUtil.build("Expense deleted", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUsername(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = bearerToken.substring(7);
        return jwtTokenProvider.getUsername(token);
    }
}
