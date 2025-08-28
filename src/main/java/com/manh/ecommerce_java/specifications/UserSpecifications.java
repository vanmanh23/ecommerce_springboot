package com.manh.ecommerce_java.specifications;

import com.manh.ecommerce_java.dtos.UserFilterRequestDTO;
import com.manh.ecommerce_java.models.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {
    public static Specification<User> searchByCondition(UserFilterRequestDTO filterInput) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                // Filter by name
                String inputSearch = filterInput.getInputSearch();
                if (inputSearch != null && !inputSearch.equals("")) {
                    Predicate predicateSearch = criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("name"), "%" + inputSearch.trim() + "%"),
                            criteriaBuilder.like(root.<String>get("email"), "%" + inputSearch.trim() + "%")
                    );
                    predicates.add(predicateSearch);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
            }
        };
    }
}
