package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.DataTableResponseDTO;
import com.manh.ecommerce_java.dtos.ProductFilterRequestDTO;
import com.manh.ecommerce_java.dtos.ProductRequestDTO;
import com.manh.ecommerce_java.models.Product;
import com.manh.ecommerce_java.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllProducts(ProductFilterRequestDTO productFilterRequestDTO) {
        DataTableResponseDTO<Product> products = productService.getAllProducts(productFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.getAll", products);
        return ResponseEntity.status(200).body(baseResponse);
    }
    @PostMapping()
    public ResponseEntity<BaseResponse> addProduct(ProductRequestDTO productRequestDTO) {
        Product savedProduct = productService.createProduct(productRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.create-product", savedProduct);
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponse> getProductById(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.getById", product);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
    @PutMapping("/{productId}")
    public ResponseEntity<BaseResponse> updateProduct(@PathVariable Integer productId,
                                                      @ModelAttribute ProductRequestDTO productRequestDTO) {
        Product savedProduct = productService.updateProduct(productId, productRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.update-product", savedProduct);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponse> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("product.success.delate-product");
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
