package com.manh.ecommerce_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class O2authRequestDTO {
    private String providerName;
    private String providerId;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;
    private Set<String> roles;
}

