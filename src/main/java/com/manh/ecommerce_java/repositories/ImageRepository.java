package com.manh.ecommerce_java.repositories;

import com.manh.ecommerce_java.models.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Transactional
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Modifying
    @Query("DELETE FROM Image e WHERE e.id = ?1 AND e.isPrimary = null")
    void deleteByProductIdAndIsNotPrimary(Integer productId);

    @Modifying
    @Query("DELETE FROM Image e WHERE e.id = ?1 AND e.isPrimary = true")
    void deleteByProductIdAndIsPrimary(Integer productId);

    @Query("Select e from Image e WHERE e.id = ?1 AND e.isPrimary = ?2")
    List<Image> findByProductIdAndIsPrimary(Integer productId , Boolean isPrimary);

    List<Image> findByProductId(Integer productId);

}
