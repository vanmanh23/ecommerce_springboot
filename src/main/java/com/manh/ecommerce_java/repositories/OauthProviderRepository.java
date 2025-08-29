package com.manh.ecommerce_java.repositories;

import com.manh.ecommerce_java.models.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthProviderRepository extends JpaRepository<OauthProvider, Integer> {
    Optional<OauthProvider> findByProviderNameAndProviderId(String providerName, String providerId);
}
