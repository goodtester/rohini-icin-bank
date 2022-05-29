package com.rohini.icinbank.repository;

import java.util.List;
import java.util.Optional;

import com.rohini.icinbank.domain.model.ExpenseDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseDetailRepository extends JpaRepository<ExpenseDetail, Long> {
    @Query(value = "SELECT * FROM expense_detail ed INNER JOIN expense e ON ed.expense_id = e.id INNER JOIN user u ON e.user_id = u.id WHERE u.username = ? AND ed.deleted = FALSE", nativeQuery = true)
    List<ExpenseDetail> getExpenseDetailsByUsername(String username);

    @Query(value = "SELECT * FROM expense_detail ed INNER JOIN expense e ON ed.expense_id = e.id INNER JOIN user u ON e.user_id = u.id WHERE u.username = ?2 AND ed.id = ?1 AND ed.deleted = FALSE", nativeQuery = true)
    Optional<ExpenseDetail> getExpenseDetailByUsername(Long id, String username);

    @Modifying
    @Query(value = "UPDATE expense_detail SET deleted = TRUE WHERE category_id = ?", nativeQuery =  true)
    void deleteExpenseDetailByCategory(Long id);

    @Modifying
    @Query(value = "UPDATE expense_detail SET deleted = TRUE WHERE expense_id = ?", nativeQuery =  true)
    void deleteExpenseDetailByExpense(Long id);
}
