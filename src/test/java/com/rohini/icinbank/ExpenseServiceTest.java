package com.rohini.icinbank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rohini.icinbank.domain.common.ExpenseInput;
import com.rohini.icinbank.domain.model.Expense;
import com.rohini.icinbank.domain.model.Payment;
import com.rohini.icinbank.domain.model.User;
import com.rohini.icinbank.repository.ExpenseDetailRepository;
import com.rohini.icinbank.repository.ExpenseRepository;
import com.rohini.icinbank.repository.PaymentRepository;
import com.rohini.icinbank.repository.UserRepository;
import com.rohini.icinbank.service.ExpenseService;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExpenseService.class)
public class ExpenseServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private Long id;
    private String username;
    
    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private ExpenseDetailRepository expenseDetailRepository;

    @Autowired
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = EASY_RANDOM.nextObject(Long.class);
        username = EASY_RANDOM.nextObject(String.class);
    }

    @Test
    void getAllSuccess_Test() {
        List<Expense> expense = EASY_RANDOM.objects(Expense.class, 2)
            .collect(Collectors.toList());

        when(expenseRepository.getExpensesByUsername(username))
            .thenReturn(expense);

        var result =  expenseService.getExpense(username);

        assertEquals(expense, result);
    }

    @Test
    void getIdSuccess_Test() {
        Expense expense = new Expense();

        when(expenseRepository.getExpenseByUsername(id, username))
            .thenReturn(Optional.of(expense));
            
        var result = expenseService.getExpense(username, id);

        assertEquals(expense, result);
    }

    @Test
    void getIdException_Test() {
        assertThrows(RuntimeException.class, () -> {
            expenseService.getExpense(username, id);
        });
    }

    @Test
    void postSuccess_Test() {
        ExpenseInput expenseInput = EASY_RANDOM.nextObject(ExpenseInput.class);
        Expense expense = new Expense();

        expense.setName(expenseInput.getName());
        expense.setExpenseDate(LocalDateTime.now());

        User user = new User();

        when(userRepository.findByUsername(username))
            .thenReturn(user);

        expense.setUser(user);

        Payment payment = new Payment();

        when(paymentRepository.searchById(expenseInput.getPaymentId()))
            .thenReturn(Optional.of(payment));

        expense.setPayment(payment);

        when(expenseRepository.save(expense))
            .thenReturn(expense);

        var result = expenseService.postExpense(username, expenseInput);

        expense.setExpenseDate(result.getExpenseDate());
        
        assertEquals(expense, result);
    }

    @Test
    void postException_Test() {
        ExpenseInput expenseInput = EASY_RANDOM.nextObject(ExpenseInput.class);

        assertThrows(RuntimeException.class, () -> {
            expenseService.postExpense(username, expenseInput);
        });
    }

    @Test
    void updateSuccess_Test() {
        ExpenseInput expenseInput = EASY_RANDOM.nextObject(ExpenseInput.class);
        Expense expense = EASY_RANDOM.nextObject(Expense.class);

        expense.setId(id);

        when(expenseRepository.getExpenseByUsername(id, username))
            .thenReturn(Optional.of(expense));

        expense.getUser().setUsername(username);

        expense.setName(expenseInput.getName());

        Payment payment = EASY_RANDOM.nextObject(Payment.class);

        when(paymentRepository.searchById(expenseInput.getPaymentId()))
            .thenReturn(Optional.of(payment));

        expense.setPayment(payment);

        when(expenseRepository.save(expense))
            .thenReturn(expense);

        var result = expenseService.updateExpense(username, id, expenseInput);

        assertEquals(expense, result);
    }

    @Test
    void updateException_Test() {
        ExpenseInput expenseInput = EASY_RANDOM.nextObject(ExpenseInput.class);

        assertThrows(RuntimeException.class, () -> {
            expenseService.updateExpense(username, id, expenseInput);
        });
    }

    @Test
    void updateExceptionUsername_Test() {
        ExpenseInput expenseInput = EASY_RANDOM.nextObject(ExpenseInput.class);
        Expense expense = EASY_RANDOM.nextObject(Expense.class);

        when(expenseRepository.getExpenseByUsername(id, username))
            .thenReturn(Optional.of(expense));

        assertThrows(RuntimeException.class, () -> {
            expenseService.updateExpense(username, id, expenseInput);
        });
    }

    @Test
    void updateExceptionPayment_Test() {
        ExpenseInput expenseInput = EASY_RANDOM.nextObject(ExpenseInput.class);
        Expense expense = EASY_RANDOM.nextObject(Expense.class);

        when(expenseRepository.getExpenseByUsername(id, username))
            .thenReturn(Optional.of(expense));

        expense.getUser().setUsername(username);

        assertThrows(RuntimeException.class, () -> {
            expenseService.updateExpense(username, id, expenseInput);
        });
    }

    @Test
    void deleteSuccess_Test() {
        Expense expense = EASY_RANDOM.nextObject(Expense.class);

        when(expenseRepository.getExpenseByUsername(id, username))
            .thenReturn(Optional.of(expense));

        expense.getUser().setUsername(username);

        doNothing()
            .when(expenseRepository).deleteById(id);

        expenseService.deleteExpense(username, id);

        verify(expenseRepository).deleteById(id);
    }

    @Test
    void deleteExceptionUsername_Test() {
        Expense expense = EASY_RANDOM.nextObject(Expense.class);

        when(expenseRepository.getExpenseByUsername(id, username))
            .thenReturn(Optional.of(expense));

        assertThrows(RuntimeException.class, () -> {
                expenseService.deleteExpense(username, id);
             });        
    }

    @Test
    void deleteException_Test() {
        assertThrows(RuntimeException.class, () -> {
                expenseService.deleteExpense(username, id);
             });        
    }
}
