package com.manh.ecommerce_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private UUID userId;

    private String accessToken;

    private String refreshToken;
}