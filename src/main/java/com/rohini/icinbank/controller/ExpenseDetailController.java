package com.rohini.icinbank.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rohini.icinbank.domain.common.ExpenseDetailInput;
import com.rohini.icinbank.domain.model.ExpenseDetail;
import com.rohini.icinbank.security.JwtTokenProvider;
import com.rohini.icinbank.service.ExpenseDetailService;
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
public class ExpenseDetailController {
    private String username;
    @Autowired
    private ExpenseDetailService expenseDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/expense-detail")
    public ResponseEntity<?> getExpenseDetail(HttpServletRequest request) {
        try {
            username = getUsername(request);
            List<ExpenseDetail> expenseDetail = expenseDetailService.getExpenseDetail(username);
            return ResponseUtil.build("Get all expense detail", expenseDetail, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/expense-detail/{id}")
    public ResponseEntity<?> getExpenseDetail(HttpServletRequest request, @PathVariable("id") Long id) {
        try {
            username = getUsername(request);
            ExpenseDetail expenseDetail = expenseDetailService.getExpenseDetail(username, id);
            return ResponseUtil.build("Get expense detail by id", expenseDetail, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/expense-detail")
    public ResponseEntity<?> postExpenseDetail (HttpServletRequest request, @RequestBody ExpenseDetailInput req) {
        try {
            username = getUsername(request);
            ExpenseDetail expenseDetail = expenseDetailService.postExpenseDetail(username, req);
            return ResponseUtil.build("Expense detail saved", expenseDetail, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/expense-detail/{id}")
    public ResponseEntity<?> putExpenseDetail (HttpServletRequest request, @PathVariable("id") Long id, @RequestBody ExpenseDetailInput req) {
        try {
            username = getUsername(request);
            ExpenseDetail expenseDetail = expenseDetailService.updateExpenseDetail(username, id, req);
            return ResponseUtil.build("Expense detail saved", expenseDetail, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/expense-detail/{id}")
    public ResponseEntity<?> deleteExpenseDetail(HttpServletRequest request, @PathVariable("id") Long id) {
        try {
            username = getUsername(request);
            expenseDetailService.deleteExpenseDetail(username, id);
            return ResponseUtil.build("Expense detail deleted", null, HttpStatus.OK);
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
