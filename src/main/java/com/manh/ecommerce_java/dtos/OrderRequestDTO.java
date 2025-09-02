package com.manh.ecommerce_java.dtos;

import com.manh.ecommerce_java.models.EPaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    @NotNull
    private UUID userId;

    @NotNull
    private Float totalPrice;


    private String orderStatus;

    @NotNull
    private Integer paymentMethodId;


    private EPaymentStatus paymentStatus;

    @NotNull
    private String nameReceiver;

    @NotNull
    private String phoneReceiver;

    @NotNull
    private String addressReceiver;

    private String trackingNumber;

    private String trackerId;

}

