package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.DataTableResponseDTO;
import com.manh.ecommerce_java.dtos.OrderFilterRequestDTO;
import com.manh.ecommerce_java.dtos.OrderRequestDTO;
import com.manh.ecommerce_java.exceptions.ResourceNotFoundException;
import com.manh.ecommerce_java.models.Order;
import com.manh.ecommerce_java.models.PaymentMethod;
import com.manh.ecommerce_java.models.User;
import com.manh.ecommerce_java.repositories.OrderRepository;
import com.trackingmore.model.tracking.Tracking;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TrackingShipmentService shipmentService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private EmailService emailService;
    public DataTableResponseDTO<Order> getAllOrders(OrderFilterRequestDTO orderFilterRequestDTO) {
        Sort sortByAndOrder = orderFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(orderFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(orderFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(orderFilterRequestDTO.getPageNumber(), orderFilterRequestDTO.getPageSize(), sortByAndOrder);
        Page<Order> pageOrders = orderRepository.findAll(pageDetails);
        List<Order> orders = pageOrders.getContent();
        DataTableResponseDTO<Order> orderResponse = new DataTableResponseDTO<Order>();
        orderResponse.setContent(orders);
        orderResponse.setPageNumber(pageOrders.getNumber());
        orderResponse.setPageSize(pageOrders.getSize());
        orderResponse.setTotalElements(pageOrders.getTotalElements());
        orderResponse.setTotalPages(pageOrders.getTotalPages());
        orderResponse.setLastPage(pageOrders.isLast());
        return orderResponse;
    }

    public Order getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
//        if (order.getTrackingNumber() != null) {
//            Tracking tracking = shipmentService.getTrackingByTrackingNumber(order.getTrackingNumber());
//            order.setOrderStatus(tracking.getDeliveryStatus());
////            order.setTrackInfo(tracking.getOriginInfo().getTrackinfo());
//            orderRepo.save(order);
//        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    public Order createOrder(OrderRequestDTO orderRequestDTO) throws Exception {
        User user = userService.getUserById(orderRequestDTO.getUserId());
        PaymentMethod paymentMethod = paymentMethodService.findPaymentMethodById(orderRequestDTO.getPaymentMethodId());
        Order order = modelMapper.map(orderRequestDTO, Order.class);
        order.setUser(user);
        order.setPaymentMethod(paymentMethod);
        order.setCreatedAt(LocalDateTime.now());
        Order newOrder = orderRepository.save(order);

        emailService.sendEmailFromTemplate(newOrder.getUser().getEmail(), "mail-order", "Bạn đã đặt hàng thành công từ shop HLN", newOrder);
        return newOrder;
    }

    public Order updateOrder(Integer orderId, OrderRequestDTO orderRequestDTO) {
        Order order = getOrderById(orderId);
        User user = userService.getUserById(orderRequestDTO.getUserId());
        order.setUser(user);

        order.setOrderStatus(orderRequestDTO.getOrderStatus());
        order.setTotalPrice(orderRequestDTO.getTotalPrice());
        order.setPaymentStatus(orderRequestDTO.getPaymentStatus());
        order.setNameReceiver(orderRequestDTO.getNameReceiver());
        order.setPhoneReceiver(orderRequestDTO.getPhoneReceiver());
        order.setAddressReceiver(orderRequestDTO.getAddressReceiver());
        order.setUpdatedAt(LocalDateTime.now());

   return orderRepository.save(order);
    }

    public void deleteOrder(Integer orderId) {
        Order existingOrder = getOrderById(orderId);
        orderRepository.delete(existingOrder);
    }


    public void updateAllOrderStatusFromTracking() {
        List<Order> orders = orderRepository.findAllByOrderNotInStatus(Arrays.asList("delivered", "canceled"));
        System.out.println("------" + orders);
        for (Order order : orders) {
            if (order.getTrackingNumber() != null) {
                Tracking tracking = shipmentService.getTrackingByTrackingNumber(order.getTrackingNumber());
                order.setOrderStatus(tracking.getDeliveryStatus());
                orderRepository.save(order);
            }
        }
    }

    public String updateTrackingNumberForOrder(int orderId, String trackingNumber, String courierCode) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);
        shipmentService.createTracking(trackingNumber, courierCode);
        return "update tracking-number success";
    }
}
