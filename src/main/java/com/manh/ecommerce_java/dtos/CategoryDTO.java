package com.manh.ecommerce_java.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private int id;
    @NotBlank(message = "{error.CategoryRequestDTO.name.blank}")
    @Size(max = 100, message = "The category name should not exceed 100 words.")
    private String name;
}
