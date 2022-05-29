package com.rohini.icinbank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rohini.icinbank.domain.common.ExpenseDetailInput;
import com.rohini.icinbank.domain.model.Category;
import com.rohini.icinbank.domain.model.Expense;
import com.rohini.icinbank.domain.model.ExpenseDetail;
import com.rohini.icinbank.repository.CategoryRepository;
import com.rohini.icinbank.repository.ExpenseDetailRepository;
import com.rohini.icinbank.repository.ExpenseRepository;
import com.rohini.icinbank.service.ExpenseDetailService;

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
@SpringBootTest(classes = ExpenseDetailService.class)
public class ExpenseDetailServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private Long id;
    private String username;
    
    @MockBean
    private ExpenseDetailRepository expenseDetailRepository;

    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseDetailService expenseDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = EASY_RANDOM.nextObject(Long.class);
        username = EASY_RANDOM.nextObject(String.class);
    }

    @Test
    void getAllSuccess_Test() {
        List<ExpenseDetail> expenseDetails = EASY_RANDOM.objects(ExpenseDetail.class, 2)
            .collect(Collectors.toList());

        when(expenseDetailRepository.getExpenseDetailsByUsername(username))
            .thenReturn(expenseDetails);

        var result =  expenseDetailService.getExpenseDetail(username);

        assertEquals(expenseDetails, result);
    }

    @Test
    void getIdSuccess_Test() {
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
            .thenReturn(Optional.of(expenseDetail));

        var result = expenseDetailService.getExpenseDetail(username, id);

        assertEquals(expenseDetail, result);
    }

    @Test
    void getIdException_Test() {
        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.getExpenseDetail(username, id);
        });
    }

    @Test
    void postSuccess_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);
        ExpenseDetail expenseDetail = new ExpenseDetail();
        Category category = new Category();

        expenseDetail.setName(detailInput.getName());
        expenseDetail.setAmount(detailInput.getAmount());

        Expense expense = new Expense();

        when(expenseRepository.getExpenseByUsername(detailInput.getExpenseId(), username))
            .thenReturn(Optional.of(expense));

        expenseDetail.setExpense(expense);

        when(categoryRepository.searchById(detailInput.getCategoryId()))
            .thenReturn(Optional.of(category));

        expenseDetail.setCategory(category);

        when(expenseDetailRepository.save(expenseDetail))
            .thenReturn(expenseDetail);

        Double totalAmount = 0.0;

        when(expenseRepository.getTotalAmount(detailInput.getExpenseId()))
            .thenReturn(totalAmount);

        totalAmount += detailInput.getAmount();

        doNothing()
            .when(expenseRepository).updateTotalAmount(detailInput.getExpenseId(), totalAmount);

        var result = expenseDetailService.postExpenseDetail(username, detailInput);

        assertEquals(expenseDetail, result);
    }

    @Test
    void postException_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);

        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.postExpenseDetail(username, detailInput);
        });        
    }

    @Test
    void postExceptionCategory_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);
        Expense expense = new Expense();

        when(expenseRepository.getExpenseByUsername(detailInput.getExpenseId(), username))
            .thenReturn(Optional.of(expense));

        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.postExpenseDetail(username, detailInput);
        });         
    }

    @Test
    void updateSuccess_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        expenseDetail.setId(id);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
            .thenReturn(Optional.of(expenseDetail));
             
        expenseDetail.getExpense().getUser().setUsername(username);

        Expense expense = EASY_RANDOM.nextObject(Expense.class);

        expense.getUser().setUsername(username);

        when(expenseRepository.getExpenseByUsername(detailInput.getExpenseId(), username))
            .thenReturn(Optional.of(expense));

        expenseDetail.setExpense(expense);

        Double totalAmount = 0.0;

        when(expenseRepository.getTotalAmount(detailInput.getExpenseId()))
            .thenReturn(totalAmount);
        
        totalAmount -= expenseDetail.getAmount();

        totalAmount += detailInput.getAmount();
        
        expenseDetail.setAmount(detailInput.getAmount());

        doNothing()
            .when(expenseRepository).updateTotalAmount(detailInput.getExpenseId(), totalAmount);

        Category category = EASY_RANDOM.nextObject(Category.class);

        when(categoryRepository.searchById(detailInput.getCategoryId()))
            .thenReturn(Optional.of(category));

        expenseDetail.setCategory(category);

        when(expenseDetailRepository.save(expenseDetail))
            .thenReturn(expenseDetail);

        var result = expenseDetailService.updateExpenseDetail(username, id, detailInput);

        assertEquals(expenseDetail, result);
    }

    @Test
    void updateException_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);

        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.updateExpenseDetail(username, id, detailInput);
        });
    }

    @Test
    void updateExceptionUsername_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
             .thenReturn(Optional.of(expenseDetail));        

        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.updateExpenseDetail(username, id, detailInput);
        });
    }

    @Test
    void updateExceptionExpense_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
             .thenReturn(Optional.of(expenseDetail));
             
        expenseDetail.getExpense().getUser().setUsername(username);

        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.updateExpenseDetail(username, id, detailInput);
        });
    }

    @Test
    void updateExceptionCategory_Test() {
        ExpenseDetailInput detailInput = EASY_RANDOM.nextObject(ExpenseDetailInput.class);
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
             .thenReturn(Optional.of(expenseDetail));
             
        expenseDetail.getExpense().getUser().setUsername(username);

        Expense expense = new Expense();

        when(expenseRepository.getExpenseByUsername(detailInput.getExpenseId(), username))
             .thenReturn(Optional.of(expense));

        Double totalAmount = 0.0;

        when(expenseRepository.getTotalAmount(detailInput.getExpenseId()))
            .thenReturn(totalAmount);

        doNothing()
            .when(expenseRepository).updateTotalAmount(detailInput.getExpenseId(), totalAmount);

        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.updateExpenseDetail(username, id, detailInput);
        });
    }

    @Test
    void deleteSuccess_Test() {
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
            .thenReturn(Optional.of(expenseDetail));

        expenseDetail.getExpense().getUser().setUsername(username);
        
        doNothing()
            .when(expenseDetailRepository).deleteById(id);

        expenseDetailService.deleteExpenseDetail(username, id);

        verify(expenseDetailRepository).deleteById(id);
    }

    @Test
    void deleteExceptionUsername_Test() {
        ExpenseDetail expenseDetail = EASY_RANDOM.nextObject(ExpenseDetail.class);

        when(expenseDetailRepository.getExpenseDetailByUsername(id, username))
            .thenReturn(Optional.of(expenseDetail));
        
        assertThrows(RuntimeException.class, () -> {
           expenseDetailService.deleteExpenseDetail(username, id);
        });
    }

    @Test
    void deleteException_Test() {
        assertThrows(RuntimeException.class, () -> {
            expenseDetailService.deleteExpenseDetail(username, id);
        });
    }
}
