package com.rohini.icinbank.controller;

import java.util.List;

import com.rohini.icinbank.domain.common.CategoryInput;
import com.rohini.icinbank.domain.model.Category;
import com.rohini.icinbank.service.CategoryService;
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
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategory() {
        try {
            List<Category> category = categoryService.getCategory();
            return ResponseUtil.build("Get all category", category, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") Long id) {
        try {
            Category category = categoryService.getCategory(id); 
            return ResponseUtil.build("Get category by id", category, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/category")
    public ResponseEntity<?> postCategory(@RequestBody CategoryInput req) {
        try {
            Category category = categoryService.postCategory(req);
            return ResponseUtil.build("Category saved", category, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<?> putCategory(@PathVariable("id") Long id, @RequestBody CategoryInput req) {
        try {
            categoryService.getCategory(id);
            Category category = categoryService.updateCategory(id, req);
            return ResponseUtil.build("Category updated", category, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseUtil.build("Category deleted", null, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseUtil.build(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
