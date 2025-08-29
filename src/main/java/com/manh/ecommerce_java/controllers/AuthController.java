package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.BaseResponse;
import com.manh.ecommerce_java.dtos.LoginRequestDTO;
import com.manh.ecommerce_java.dtos.LoginResponseDTO;
import com.manh.ecommerce_java.dtos.UserRequestDTO;
import com.manh.ecommerce_java.models.RefreshToken;
import com.manh.ecommerce_java.models.User;
import com.manh.ecommerce_java.repositories.UserRepository;
import com.manh.ecommerce_java.security.JWTUtil;
import com.manh.ecommerce_java.security.UserDetailsServiceImpl;
import com.manh.ecommerce_java.services.Oauth2Service;
import com.manh.ecommerce_java.services.RefreshTokenService;
import com.manh.ecommerce_java.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Oauth2Service oauth2Service;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> registerHandler(@Valid @RequestBody UserRequestDTO userRequestDTO) throws Exception {
        String encodedPass = passwordEncoder.encode(userRequestDTO.getPassword());
        userRequestDTO.setPassword(encodedPass);
        User user = userService.registerUser(userRequestDTO);
        String token = jwtUtil.generateToken(user.getEmail());
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("register successful", token);
        return ResponseEntity.status(200).body(baseResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> loginHandler(@Valid @RequestBody LoginRequestDTO credentials) {
        UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
                credentials.getEmail(), credentials.getPassword());
        authenticationManager.authenticate(authCredentials);
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        credentials.getEmail(), credentials.getPassword()
//                )
//        );
        // UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(credentials.getEmail());
        User user = userService.getUserByEmail(credentials.getEmail());
        // Create a AccessToken
        String token = jwtUtil.generateToken(credentials.getEmail());

        // Create a RefreshToken
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), token, refreshToken.getToken());
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("login successful", loginResponseDTO);
        return ResponseEntity.status(200).body(baseResponse);
    }
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse> logout(@RequestParam String refreshToken) {
        System.out.println(refreshToken);
        refreshTokenService.revokeRefreshToken(refreshToken);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("Logged out successfully");
        return ResponseEntity.ok().body(baseResponse);
    }

}
