package com.manh.ecommerce_java.dtos;

import com.manh.ecommerce_java.models.EPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequestDTO  extends PageableAndSortParam {
    private String inputSearch;

    private String orderStatus;

    private EPaymentStatus ePaymentStatus;
}
