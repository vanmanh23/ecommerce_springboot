package com.manh.ecommerce_java.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "nameReceiver", nullable = false)
    @NotNull(message = "{error.order.nameReceiver.null}")
    @NotBlank(message = "{error.order.nameReceiver.blank}")
    private String nameReceiver;

    @Column(name = "phoneReceiver", nullable = false)
    @NotNull(message = "{error.order.phoneReceiver.null}")
    @NotBlank(message = "{error.order.phoneReceiver.blank}")
    private String phoneReceiver;

    @Column(name = "addressReceiver", nullable = false)
    @NotNull(message = "{error.order.addressReceiver.null}")
    @NotBlank(message = "{error.order.addressReceiver.blank}")
    private String addressReceiver;

    @Column(name = "totalPrice")
    private Float totalPrice;

    @Column(name = "paymentStatus")
    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;

    @Column(name = "orderStatus")
    private String orderStatus;

    @Column(name = "trackingNumber")
    private String trackingNumber;

    @Column(name = "trackerId")
    private String trackerId;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne( fetch = FetchType.EAGER)
    @NotNull(message = "{error.order.paymentMethod.null}")
    @JoinColumn(name = "paymentMethod_id", referencedColumnName = "id", nullable = false)
    private PaymentMethod paymentMethod;

}
