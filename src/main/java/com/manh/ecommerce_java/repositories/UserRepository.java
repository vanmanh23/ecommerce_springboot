package com.manh.ecommerce_java.repositories;

import com.manh.ecommerce_java.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Page<User> findAll(Specification<User> spec, Pageable pageable);
//    @Query("SELECT e FROM User e WHERE e.email LIKE  %:email% ")
//    Optional<User> findByEmail(@Param("email") String email);
    Optional<User> findByEmail(String email);
}
