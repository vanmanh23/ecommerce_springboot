package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.CategoryDTO;
import com.manh.ecommerce_java.exceptions.ResourceNotFoundException;
import com.manh.ecommerce_java.models.Category;
import com.manh.ecommerce_java.models.Product;
import com.manh.ecommerce_java.repositories.CategoryRepository;
import com.manh.ecommerce_java.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepoProduct;


    public List<Category> getAllCategories() throws Exception {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

    public List<Category> findCategoryByName(String name) {
        return categoryRepository.findByNameLike(name);
    }

    public Category createCategory(CategoryDTO categoryRequestDTO) {
        Category category = modelMapper.map(categoryRequestDTO, Category.class);
        return categoryRepository.save(category);
    }

    public Category updateCategory(int categoryId, CategoryDTO categoryRequestDTO) {
        Category existingCategory = findCategoryById(categoryId);
        existingCategory.setName(categoryRequestDTO.getName());
        return categoryRepository.save(existingCategory);
    }

    public boolean checkBeforeDeleteCategory(int categoryId) {
        List<Product> products = productRepoProduct.checkBeforeDeleteCategory(categoryId);
        return products.isEmpty();
    }

    public void deleteCategory(int categoryId) {
        Category existingCategory = findCategoryById(categoryId);
        categoryRepository.delete(existingCategory);
    }
}
