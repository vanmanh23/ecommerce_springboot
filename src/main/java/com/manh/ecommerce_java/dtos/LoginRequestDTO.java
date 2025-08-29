package com.manh.ecommerce_java.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @Email
    private String email;

    @Size(min = 6)
    private String password;

    private String accessToken;

    private String refreshToken;
}
