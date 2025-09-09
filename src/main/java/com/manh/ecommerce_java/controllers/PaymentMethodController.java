package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.PaymentMethodRequestDTO;
import com.manh.ecommerce_java.models.PaymentMethod;
import com.manh.ecommerce_java.services.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/paymentMethods")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("")
    public ResponseEntity<BaseResponse> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodService.getAllPaymentMethods();
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.getAll", paymentMethods);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findPaymentMethodById(@PathVariable("id") UUID id) {
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(id);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.getById", paymentMethod);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createPaymentMethod(@Valid @RequestBody PaymentMethodRequestDTO requestDTO) {
        PaymentMethod createdPaymentMethod = paymentMethodService.createPaymentMethod(requestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.create", createdPaymentMethod);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePaymentMethod(@PathVariable UUID id, @Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod updatedPaymentMethod = paymentMethodService.updatePaymentMethod(id, paymentMethodRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.update", updatedPaymentMethod);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePaymentMethod(@PathVariable UUID id) {
        paymentMethodService.deletePaymentMethod(id);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("payment-method.success.delete");
        return ResponseEntity.status(200).body(baseResponse);
    }
}
