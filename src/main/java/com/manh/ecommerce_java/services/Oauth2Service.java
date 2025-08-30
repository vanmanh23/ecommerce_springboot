package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.O2authRequestDTO;
import com.manh.ecommerce_java.models.OauthProvider;
import com.manh.ecommerce_java.models.RefreshToken;
import com.manh.ecommerce_java.models.Role;
import com.manh.ecommerce_java.models.User;
import com.manh.ecommerce_java.repositories.OauthProviderRepository;
import com.manh.ecommerce_java.repositories.RefreshTokenRepository;
import com.manh.ecommerce_java.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class Oauth2Service {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OauthProviderRepository oauthProviderRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    public User processOAuth2User(O2authRequestDTO o2authRequestDTO) {
        // Get user info from provider
        String providerId = o2authRequestDTO.getProviderId();
        String name = o2authRequestDTO.getName();
        String email = o2authRequestDTO.getEmail();
        // Check if user already exists
        Optional<OauthProvider> oauthProviderOptional = oauthProviderRepository.findByProviderNameAndProviderId(o2authRequestDTO.getProviderName(), providerId);
        User newUser = null;
        if (oauthProviderOptional.isPresent()) {
            // User already exists, update info if needed
            User user = oauthProviderOptional.get().getUser();
            boolean needUpdate = false;
            if (!user.getName().equals(name)) {
                user.setName(name);
                needUpdate = true;
            }
            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
                needUpdate = true;
            }
            if (needUpdate) {
                newUser = userRepository.save(user);
            } else {
                newUser = user;
            }
        } else {
            // User doesn't exist, create new user
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            Set<Role> roles = new HashSet<>();
            for (String roleName : o2authRequestDTO.getRoles()) {
                Role role = roleService.findRoleByName(roleName);
                roles.add(role);
            }
            user.setRoles(roles);
            newUser = userRepository.save(user);
            OauthProvider oauthProvider = new OauthProvider();
            oauthProvider.setProviderName(o2authRequestDTO.getProviderName());
            oauthProvider.setProviderId(providerId);
            oauthProvider.setUser(newUser);
            oauthProviderRepository.save(oauthProvider);
        }

        return newUser;

    }

    public RefreshToken processOAuth2Tokens( User user) {
        // Save refresh token in database
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
        newRefreshToken.setToken(UUID.randomUUID().toString());
        newRefreshToken.setExpiryDate(Instant.now().plus(Duration.ofDays(7)));  // assuming the refresh token is valid for 7 days
        return refreshTokenRepository.save(newRefreshToken);
        // For this example, we will not store the access token in the database, as it's typically short-lived and used directly by the client.
        // However, you should send the access token back to the client application in your API response.
    }
}
