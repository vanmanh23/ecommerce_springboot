package com.manh.ecommerce_java.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "payment_method")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "name",length = 100)
    @NotNull(message = "{error.payment_method.name.null}")
    @NotBlank(message = "{error.payment_method.name.blank}")
    @Size(max = 100, message = "{error.payment_method.name.size}")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "isEnable")
    private Boolean isEnable;

    @JsonIgnore
    @OneToMany(mappedBy = "paymentMethod" )
    private Set<Order> orders;
}
