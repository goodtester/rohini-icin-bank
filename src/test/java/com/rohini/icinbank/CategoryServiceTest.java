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

import com.rohini.icinbank.domain.common.CategoryInput;
import com.rohini.icinbank.domain.model.Category;
import com.rohini.icinbank.repository.CategoryRepository;
import com.rohini.icinbank.repository.ExpenseDetailRepository;
import com.rohini.icinbank.service.CategoryService;

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
@SpringBootTest(classes = CategoryService.class)
public class CategoryServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private Long id;
    
    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ExpenseDetailRepository expenseDetailRepository;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = EASY_RANDOM.nextObject(Long.class);
    }

    @Test
    void getAllSuccess_Test() {
        List<Category> categories = EASY_RANDOM.objects(Category.class, 2)
            .collect(Collectors.toList());
            
        when(categoryRepository.findAll())
            .thenReturn(categories);

        var result = categoryService.getCategory();

        verify(categoryRepository, times(1)).findAll();

        assertEquals(categories, result);
    }

    @Test
    void getIdSuccess_Test() {
        Category category = EASY_RANDOM.nextObject(Category.class);

        when(categoryRepository.findById(category.getId()))
            .thenReturn(Optional.of(category));

        var result = categoryService.getCategory(category.getId());

        assertEquals(category, result);
    }

    @Test
    void getIdException_Test() {
        assertThrows(RuntimeException.class, () -> {
            categoryService.getCategory(id);
        });
    }

    @Test
    void postSuccess_Test() {
        CategoryInput categoryInput = new CategoryInput();
        Category category = new Category();

        categoryInput.setName("Food & Beverage");        
        category.setName(categoryInput.getName());

        when(categoryRepository.save(category))
            .thenReturn(category);

        var result = categoryService.postCategory(categoryInput);

        assertEquals(category, result);
    }

    @Test
    void updateSuccess_Test() {
        CategoryInput categoryInput = new CategoryInput();
        categoryInput.setName("Foods");
        
        Category category = new Category();        

        doReturn(category)
            .when(categoryRepository).getById(id);

        category.setName(categoryInput.getName());

        when(categoryRepository.save(category))
            .thenReturn(category);

        var result = categoryService.updateCategory(id, categoryInput);

        assertEquals(category, result);
    }

    @Test
    void deleteSuccess_Test() {
        doNothing()
           .when(categoryRepository).deleteById(id);

        doNothing()
            .when(expenseDetailRepository).deleteExpenseDetailByCategory(id);

        categoryService.deleteCategory(id);

        verify(categoryRepository).deleteById(id);
    }

    @Test
    void deleteException_Test() {
        doThrow(RuntimeException.class)
            .when(categoryRepository).deleteById(id);
        doThrow(RuntimeException.class)
            .when(expenseDetailRepository).deleteExpenseDetailByCategory(id);

        assertThrows(RuntimeException.class, () -> {
            categoryService.deleteCategory(id);
        });
    }
}