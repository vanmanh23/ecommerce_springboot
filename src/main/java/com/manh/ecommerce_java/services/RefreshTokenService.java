package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.models.RefreshToken;
import com.manh.ecommerce_java.models.User;
import com.manh.ecommerce_java.repositories.RefreshTokenRepository;
import com.manh.ecommerce_java.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(User user) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken;
        if (optionalRefreshToken.isPresent()) {
            refreshToken = optionalRefreshToken.get();
        } else {
            refreshToken = new RefreshToken();
            refreshToken.setUser(user);
        }
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(Duration.ofDays(7)));
        return refreshTokenRepository.save(refreshToken);
    }
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Transactional
    public void revokeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
