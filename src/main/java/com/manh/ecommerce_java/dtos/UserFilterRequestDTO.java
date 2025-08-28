package com.manh.ecommerce_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilterRequestDTO extends PageableAndSortParam{
    private String inputSearch;
}
