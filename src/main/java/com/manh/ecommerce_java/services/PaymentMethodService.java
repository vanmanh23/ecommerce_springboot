package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.PaymentMethodRequestDTO;
import com.manh.ecommerce_java.exceptions.ResourceNotFoundException;
import com.manh.ecommerce_java.models.PaymentMethod;
import com.manh.ecommerce_java.repositories.PaymentMethodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepo.findAll();
    }

    public PaymentMethod findPaymentMethodById(UUID paymentMethodId) {
        return paymentMethodRepo.findById(paymentMethodId)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentMethod not found with id: " + paymentMethodId));
    }


    public PaymentMethod createPaymentMethod(PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod paymentMethod = modelMapper.map(paymentMethodRequestDTO, PaymentMethod.class);
        return paymentMethodRepo.save(paymentMethod);
    }

    public PaymentMethod updatePaymentMethod(UUID paymentMethodId, PaymentMethodRequestDTO paymentMethodRequestDTO) {
        PaymentMethod existingPaymentMethod = findPaymentMethodById(paymentMethodId);
        existingPaymentMethod.setName(paymentMethodRequestDTO.getName());
        existingPaymentMethod.setDescription(paymentMethodRequestDTO.getDescription());
        existingPaymentMethod.setIsEnable(paymentMethodRequestDTO.getIsEnable());
        return paymentMethodRepo.save(existingPaymentMethod);
    }

    public void deletePaymentMethod(UUID paymentMethodId) {
        PaymentMethod existingPaymentMethod = findPaymentMethodById(paymentMethodId);
        paymentMethodRepo.delete(existingPaymentMethod);
    }
}
