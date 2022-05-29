package com.rohini.icinbank.controller;

import java.util.List;

import com.rohini.icinbank.domain.common.PaymentInput;
import com.rohini.icinbank.domain.model.Payment;
import com.rohini.icinbank.service.PaymentService;
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
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payment")
    public ResponseEntity<?> getPayment() {
        try {
            List<Payment> payment = paymentService.getPayment();
            return ResponseUtil.build("Get all payment", payment, HttpStatus.OK); 
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<?> getPayment(@PathVariable("id") Long id) {
        try {
            Payment payment = paymentService.getPayment(id);
            return ResponseUtil.build("Get payment by id", payment, HttpStatus.OK); 
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<?> postPayment (@RequestBody PaymentInput req) {
        try {
            Payment payment = paymentService.postPayment(req);
            return ResponseUtil.build("Payment saved", payment, HttpStatus.OK);
        } catch (Exception e) {           
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/payment/{id}")
    public ResponseEntity<?> putPayment (@PathVariable("id") Long id, @RequestBody PaymentInput req) {
        try {
            paymentService.getPayment(id);
            Payment payment = paymentService.updatePayment(id, req);
            return ResponseUtil.build("Payment updated", payment, HttpStatus.OK);
        } catch (Exception e) {           
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/payment/{id}")
    public ResponseEntity<?> deletePayment (@PathVariable("id") Long id) {
        try {
            paymentService.deletePayment(id);
            return ResponseUtil.build("Payment deleted", null, HttpStatus.OK);
        } catch (Exception e) {           
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
