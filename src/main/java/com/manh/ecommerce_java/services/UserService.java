package com.manh.ecommerce_java.services;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.DataTableResponseDTO;
import com.manh.ecommerce_java.dtos.UserFilterRequestDTO;
import com.manh.ecommerce_java.dtos.UserRequestDTO;
import com.manh.ecommerce_java.models.Role;
import com.manh.ecommerce_java.models.User;
import com.manh.ecommerce_java.repositories.UserRepository;
import com.manh.ecommerce_java.specifications.UserSpecifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleService roleService;
    public DataTableResponseDTO<User> getAllUsers(UserFilterRequestDTO userFilterRequestDTO) {
        Sort sortByAndOrder = userFilterRequestDTO.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(userFilterRequestDTO.getSortBy()).ascending()
                : Sort.by(userFilterRequestDTO.getSortBy()).descending();
        Pageable pageDetails = PageRequest.of(userFilterRequestDTO.getPageNumber(), userFilterRequestDTO.getPageSize(), sortByAndOrder);
        Specification<User> userSpecification = UserSpecifications.searchByCondition(userFilterRequestDTO);
        Page<User> pageUsers = userRepository.findAll(userSpecification, pageDetails);
        List<User> users = pageUsers.getContent();
        DataTableResponseDTO<User> userResponse = new DataTableResponseDTO<>();
        userResponse.setContent(users);
        userResponse.setPageNumber(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElements(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());

        return userResponse;
    }
    public User registerUser(UserRequestDTO userRequestDTO) throws Exception {
        Optional<User> existedUser = userRepository.findByEmail(userRequestDTO.getEmail());
        if (existedUser.isEmpty()) {
            User user = modelMapper.map(userRequestDTO, User.class);
            Set<Role> roles = new HashSet<>();
            for (String roleName : userRequestDTO.getRoles()) {
                Role role = roleService.findRoleByName(roleName);
                roles.add(role);
            }
            user.setRoles(roles);
            return userRepository.save(user);
        } else {
            throw new Exception("Email" + userRequestDTO.getEmail() + "existed");
        }
    }
}
