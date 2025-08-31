package com.manh.ecommerce_java.repositories;

import com.manh.ecommerce_java.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "SELECT * FROM categories WHERE id = ?1", nativeQuery = true)
    Optional<Category> findById(int categoryId);
    List<Category> findByNameLike(String name);
}
