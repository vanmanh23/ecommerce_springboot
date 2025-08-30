package com.manh.ecommerce_java.repositories;

import com.manh.ecommerce_java.models.RefreshToken;
import com.manh.ecommerce_java.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByUser(User user);
    void deleteByToken(String token);
    Optional<RefreshToken> findByToken(String refreshToken);
}
