package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.DataTableResponseDTO;
import com.manh.ecommerce_java.dtos.ProductFilterRequestDTO;
import com.manh.ecommerce_java.dtos.ProductRequestDTO;
import com.manh.ecommerce_java.exceptions.ResourceNotFoundException;
import com.manh.ecommerce_java.models.Category;
import com.manh.ecommerce_java.models.Image;
import com.manh.ecommerce_java.models.Product;
import com.manh.ecommerce_java.repositories.ImageRepository;
import com.manh.ecommerce_java.repositories.ProductRepository;
import com.manh.ecommerce_java.specifications.ProductSpecifications;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ProductService {
    @Autowired
    protected ImageRepository imageRepository;
    @Autowired
    MinioService minIOService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ModelMapper modelMapper;

    public DataTableResponseDTO<Product> getAllProducts(ProductFilterRequestDTO productFilterRequestDTO) {
        Sort sortByAndOrder = productFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(productFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(productFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(productFilterRequestDTO.getPageNumber(), productFilterRequestDTO.getPageSize(), sortByAndOrder);

        Specification<Product> spec = ProductSpecifications.searchByCondition(productFilterRequestDTO);

        Page<Product> pageProducts = productRepository.findAll(spec, pageDetails);

        List<Product> products = pageProducts.getContent();

        DataTableResponseDTO<Product> productResponse = new DataTableResponseDTO<>();

        productResponse.setContent(products);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    public Product createProduct(ProductRequestDTO productRequestDTO) {
        //save product
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());
        Product product;
        product = modelMapper.map(productRequestDTO, Product.class);
        product.setCategory(category);
        Product newProduct = productRepository.save(product);

        //save Image
        String imagePrimaryUrl = minIOService.uploadFile(productRequestDTO.getImagePrimary());
        Image image1 = new Image();
        image1.setImagePath(imagePrimaryUrl);
        image1.setIsPrimary(true);
        image1.setAltText(newProduct.getName() + " - Primary Image");
        image1.setProduct(product);
        imageRepository.save(image1);
        for (MultipartFile moreImage : productRequestDTO.getMoreImages()) {
            System.out.println("Uploading image: " + moreImage.getOriginalFilename());  // In ra tên ảnh
            String imageUrl = minIOService.uploadFile(moreImage);
            Image image = new Image();
            image.setImagePath(imageUrl);
            image.setAltText(moreImage.getOriginalFilename());
            image.setProduct(product);
            imageRepository.save(image);
        }

        return getProductById(newProduct.getId());
    }

    public Product updateProduct(Integer productId, ProductRequestDTO productRequestDTO) {
        Product existingProduct = getProductById(productId);
        Category category = categoryService.findCategoryById(productRequestDTO.getCategoryId());
        existingProduct.setCategory(category);
        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setDescription(productRequestDTO.getDescription());

        List<Image> images = new ArrayList<>();
        // Nếu có ảnh chính mới, cập nhật
        if (productRequestDTO.getImagePrimary() != null) {
//            imageRepo.deleteByProductIdAndIsPrimary(productId);
            imageRepository.deleteAll(imageRepository.findByProductIdAndIsPrimary(productId, true));
            String imagePrimaryUrl = minIOService.uploadFile(productRequestDTO.getImagePrimary());
            Image image1 = new Image();
            image1.setImagePath(imagePrimaryUrl);
            image1.setIsPrimary(true);
            image1.setAltText(existingProduct.getName() + " - Primary Image");
            image1.setProduct(existingProduct);
            imageRepository.save(image1);
        }

        // Nếu có thêm ảnh mới, cập nhật
        MultipartFile[] moreImages = productRequestDTO.getMoreImages();
        if (moreImages != null && moreImages.length > 0) {
//            imageRepo.deleteByProductIdAndIsNotPrimary(productId);
            imageRepository.deleteAll(imageRepository.findByProductIdAndIsPrimary(productId, null));
            for (MultipartFile moreImage : moreImages) {
                System.out.println("Uploading image: " + moreImage.getOriginalFilename());
                String imageUrl = minIOService.uploadFile(moreImage);
                Image image = new Image();
                image.setImagePath(imageUrl);
                image.setAltText(moreImage.getOriginalFilename());
                image.setProduct(existingProduct);
                imageRepository.save(image);
            }
        }
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Integer productId) {
        Product existingProduct = getProductById(productId);
        imageRepository.deleteAll(imageRepository.findByProductId(productId));
        productRepository.delete(existingProduct);
    }

}
