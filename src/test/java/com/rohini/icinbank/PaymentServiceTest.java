package com.rohini.icinbank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rohini.icinbank.domain.common.PaymentInput;
import com.rohini.icinbank.domain.model.Payment;
import com.rohini.icinbank.repository.ExpenseRepository;
import com.rohini.icinbank.repository.PaymentRepository;
import com.rohini.icinbank.service.PaymentService;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PaymentService.class)
public class PaymentServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private Long id;
    
    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private ExpenseRepository expenseRepository;

    @Autowired
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = EASY_RANDOM.nextObject(Long.class);
    }

    @Test
    void getAllSuccess_Test() {
        List<Payment> payments = EASY_RANDOM.objects(Payment.class, 2)
            .collect(Collectors.toList());

        when(paymentRepository.findAll())
            .thenReturn(payments);

        var result = paymentService.getPayment();

        verify(paymentRepository, times(1)).findAll();
    
        assertEquals(payments, result);
    }

    @Test
    void getIdSuccess_Test() {
        Payment payment = EASY_RANDOM.nextObject(Payment.class);

        when(paymentRepository.findById(payment.getId()))
            .thenReturn(Optional.of(payment));

        var result = paymentService.getPayment(payment.getId());

        Assertions.assertEquals(payment, result);
    }

    @Test
    void getIdException_Test() {
        assertThrows(RuntimeException.class, () -> {
            paymentService.getPayment(id);
        });
    }

    @Test
    void postSuccess_Test() {
        PaymentInput paymentInput = new PaymentInput();
        Payment payment = new Payment();

        paymentInput.setCardNumber("123456789");        
        payment.setCardNumber(paymentInput.getCardNumber());

        when(paymentRepository.save(payment))
            .thenReturn(payment);

        var result = paymentService.postPayment(paymentInput);

        Assertions.assertEquals(payment, result);
    }

    @Test
    void updateSuccess_Test() {
        PaymentInput paymentInput = new PaymentInput();
        paymentInput.setCardNumber("123456789");
        
        Payment pay = new Payment();

        doReturn(pay)
            .when(paymentRepository).getById(id);

        pay.setCardNumber(paymentInput.getCardNumber());
        
        when(paymentRepository.save(pay))
            .thenReturn(pay);

        var result = paymentService.updatePayment(id, paymentInput);

        Assertions.assertEquals(pay, result);
    }

    @Test
    void updateException_Test() {
        PaymentInput paymentInput = new PaymentInput();
        paymentInput.setCardNumber("123456789");

        doThrow(RuntimeException.class)
            .when(paymentRepository).getById(id);

        assertThrows(RuntimeException.class, () -> {
            paymentService.updatePayment(id, paymentInput);
        });
    }

    @Test
    void deleteSuccess_Test() {
        doNothing()
           .when(paymentRepository).deleteById(id);

        doNothing()
            .when(expenseRepository).deleteExpenseByPayment(id);

        paymentService.deletePayment(id);

        verify(paymentRepository).deleteById(id);
    }

    @Test
    void deleteException_Test() {
        doThrow(RuntimeException.class)
            .when(paymentRepository).deleteById(id);
        doThrow(RuntimeException.class)
            .when(expenseRepository).deleteExpenseByPayment(id);

        assertThrows(RuntimeException.class, () -> {
            paymentService.deletePayment(id);
        });
    }
}
