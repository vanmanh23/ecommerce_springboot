package com.manh.ecommerce_java.dtos;

import com.manh.ecommerce_java.config.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterRequestDTO {
    private String name;

    private Float minPrice;

    private Float maxPrice;

    private List<String> categoryIds;

    private Integer pageNumber = Integer.valueOf(AppConstants.PAGE_NUMBER);

    private Integer pageSize = Integer.valueOf(AppConstants.PAGE_SIZE);

    private String sortBy = AppConstants.SORT_BY;

    private String sortOrder = AppConstants.SORT_DIR;
}