package com.manh.ecommerce_java.specifications;


import com.manh.ecommerce_java.dtos.ProductFilterRequestDTO;
import com.manh.ecommerce_java.models.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications {
    public static Specification<Product> searchByCondition(ProductFilterRequestDTO filterInput) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                // Filter by name
                String inputSearch = filterInput.getName();
                if (inputSearch != null && !inputSearch.equals("")) {
                    predicates.add(criteriaBuilder.like(root.<String>get("name"), "%" + inputSearch.trim() + "%"));
                }

                // Filter by price range
                Float maxPrice = filterInput.getMaxPrice();
                Float minPrice = filterInput.getMinPrice();
                if (minPrice != null ) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
                }

                if (maxPrice != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
                }

                // Filter by category IDs
                List<String> categoryIds = filterInput.getCategoryIds();
                if (categoryIds != null && !categoryIds.isEmpty()) {
                    predicates.add(root.get("category").get("id").in(categoryIds));
                }

                // Sorting
//                if (sortBy != null && !sortBy.isEmpty()) {
//                    if (sortBy.equalsIgnoreCase("newest")) {
//                        query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
//                    } else if (sortBy.equalsIgnoreCase("oldest")) {
//                        query.orderBy(criteriaBuilder.asc(root.get("createdAt")));
//                    }
//                    // Add more sorting options as needed
//                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }

}

