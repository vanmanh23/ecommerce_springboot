package com.manh.ecommerce_java.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_path",length = 65555)
    @NotBlank(message = "{error.image.imagePath.blank")
    private String imagePath;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "isPrimary")
    private Boolean isPrimary;

    @JsonIgnore
    @NotNull(message = "{error.image.product.null}")
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;
}

