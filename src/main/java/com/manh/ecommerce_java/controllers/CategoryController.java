package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.CategoryDTO;
import com.manh.ecommerce_java.models.Category;
import com.manh.ecommerce_java.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllCategories() throws Exception{
        List<Category> categories = categoryService.getAllCategories();
        BaseResponse baseResponse = new BaseResponse(true, "category.success.getAll", categories, null);
        return ResponseEntity.status(200).body(baseResponse);
    }
    @PostMapping
    public ResponseEntity<BaseResponse> createCategory(@Valid @RequestBody CategoryDTO categoryRequest) {
        Category category = categoryService.createCategory(categoryRequest);
        BaseResponse baseResponse = new BaseResponse(true, "category.success.createdCategory", category, null);
        return ResponseEntity.status(201).body(baseResponse);
    }
    @GetMapping("/{id_1}")
    public ResponseEntity<BaseResponse> findCategoryById(@PathVariable("id_1") int id) {
        Category category = categoryService.findCategoryById(id);
        BaseResponse baseResponse = new BaseResponse(true, "category.success.findCategoryById", category, null);
        return ResponseEntity.status(200).body(baseResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateCategory(@PathVariable int id, @Valid @RequestBody CategoryDTO CategoryDTO) {
        Category updatedCategory = categoryService.updateCategory(id, CategoryDTO);
        BaseResponse baseResponse = new BaseResponse(true, "category.success.updatedCategory", updatedCategory, null);
        return ResponseEntity.status(200).body(baseResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable int id) {
        if (categoryService.checkBeforeDeleteCategory(id)) {
            categoryService.deleteCategory(id);
            BaseResponse baseResponse = new BaseResponse(true, "category.success.deleteCategory", null, null);
            return ResponseEntity.status(200).body(baseResponse);
        } else {
            BaseResponse baseResponse = new BaseResponse(false, "category.error.deleteCategory.existProduct", null, null);
            return ResponseEntity.status(200).body(baseResponse);
        }
    }
}
