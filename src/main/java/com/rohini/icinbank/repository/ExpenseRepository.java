package com.rohini.icinbank.repository;

import java.util.List;
import java.util.Optional;

import com.rohini.icinbank.domain.model.Expense;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "SELECT * FROM expense e INNER JOIN user u ON e.user_id = u.id WHERE u.username = ? AND e.deleted = FALSE", nativeQuery = true)
    List<Expense> getExpensesByUsername(String username);

    @Query(value = "SELECT * FROM expense e INNER JOIN user u ON e.user_id = u.id WHERE u.username = ?2 AND e.id = ?1 AND e.deleted = FALSE", nativeQuery = true)
    Optional<Expense> getExpenseByUsername(Long id, String username);

    @Query(value = "SELECT total_amount FROM expense e WHERE e.id = ?", nativeQuery = true)
    Double getTotalAmount(Long id);

    @Modifying
    @Query(value = "UPDATE expense e SET e.total_amount = ?2 WHERE e.id = ?1", nativeQuery = true)
    void updateTotalAmount(Long id, Double totalAmount);

    @Modifying
    @Query(value = "UPDATE expense SET deleted = TRUE WHERE payment_id = ?", nativeQuery =  true)
    void deleteExpenseByPayment(Long id);
}
