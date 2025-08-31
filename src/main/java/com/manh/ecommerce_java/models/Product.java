package com.manh.ecommerce_java.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "{error.product.name.blank}")
    @Size(max = 255, message = "{error.product.name.size}")
    private String name;

    @Column(name = "price")
    @NotNull(message = "{error.product.price.null}")
    private Float price;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description", columnDefinition = "LONGBLOB")
    private String description;

    @Column(name = "inStock")
    private Integer inStock;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @NotNull(message = "{error.product.category.null}")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true )
    private List<Image> images ;
}
