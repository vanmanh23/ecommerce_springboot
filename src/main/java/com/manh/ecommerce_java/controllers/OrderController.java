package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.DataTableResponseDTO;
import com.manh.ecommerce_java.dtos.OrderFilterRequestDTO;
import com.manh.ecommerce_java.dtos.OrderRequestDTO;
import com.manh.ecommerce_java.models.Order;
import com.manh.ecommerce_java.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllOrders(OrderFilterRequestDTO orderFilterRequestDTO) {
        DataTableResponseDTO<Order> orders = orderService.getAllOrders(orderFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.getAll", orders);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponse> getOrderById(@PathVariable Integer orderId) {
        Order order = orderService.getOrderById(orderId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.getById", order);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @PostMapping("")
    public ResponseEntity<BaseResponse> addOrder(@Valid @RequestBody OrderRequestDTO order) throws Exception {
        Order savedOrder = orderService.createOrder(order);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.create", savedOrder);
        return ResponseEntity.status(201).body(baseResponse);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<BaseResponse> updateOrder(@PathVariable Integer orderId, @Valid @RequestBody OrderRequestDTO order) {
        Order savedOrder = orderService.updateOrder(orderId, order);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.update", savedOrder);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<BaseResponse> deleteOrder(@PathVariable Integer orderId) {
        orderService.deleteOrder(orderId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("order.success.delete");
        return ResponseEntity.status(200).body(baseResponse);
    }
}
