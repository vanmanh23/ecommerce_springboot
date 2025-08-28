package com.manh.ecommerce_java.dtos;

import com.manh.ecommerce_java.config.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableAndSortParam {

    public Integer pageNumber = Integer.valueOf(AppConstants.PAGE_NUMBER);

    public Integer pageSize = Integer.valueOf(AppConstants.PAGE_SIZE);

    public String sortBy = AppConstants.SORT_BY;

    public String sortOrder = AppConstants.SORT_DIR;

}
