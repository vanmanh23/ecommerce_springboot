package com.manh.ecommerce_java.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodRequestDTO {
    @NotBlank
    @Size(min = 3, message = "PaymentMethod name must contain atleast 3 characters")
    private String name;

    private String description;

    private Boolean isEnable;
}
