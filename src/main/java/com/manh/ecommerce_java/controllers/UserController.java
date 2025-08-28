package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.DataTableResponseDTO;
import com.manh.ecommerce_java.dtos.UserFilterRequestDTO;
import com.manh.ecommerce_java.dtos.UserRequestDTO;
import com.manh.ecommerce_java.models.User;
import com.manh.ecommerce_java.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/list")
    public ResponseEntity<BaseResponse> getAllUsers(@RequestBody UserFilterRequestDTO userFilterRequestDTO) {
        DataTableResponseDTO<User> users = userService.getAllUsers(userFilterRequestDTO);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.getAll", users);
        return ResponseEntity.status(200).body(baseResponse);
    }
    @PostMapping("")
    public ResponseEntity<BaseResponse> addUser(@Valid @RequestBody UserRequestDTO user) throws Exception {
        User savedUser = userService.registerUser(user);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("user.success.create", savedUser);
        return ResponseEntity.status(201).body(baseResponse);
    }
}
