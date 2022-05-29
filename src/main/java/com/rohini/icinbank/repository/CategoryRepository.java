package com.rohini.icinbank.repository;

import java.util.Optional;

import com.rohini.icinbank.domain.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM category c WHERE c.deleted = false AND c.id = ?", nativeQuery = true)
    Optional<Category> searchById(Long id);
}
