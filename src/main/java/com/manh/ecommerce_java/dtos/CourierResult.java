package com.manh.ecommerce_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourierResult {
        private String courierCode;
        private String courierName;
        private String courierUrl;
        private String courierPhone;
        private Object required;
}
