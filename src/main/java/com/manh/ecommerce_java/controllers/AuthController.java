package com.manh.ecommerce_java.controllers;

import com.manh.ecommerce_java.dtos.*;
import com.manh.ecommerce_java.exceptions.ResourceNotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PostMapping("/getUserInfor")
    public ResponseEntity<BaseResponse> getInformationUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        String email = jwtUtil.validateTokenAndRetrieveSubject(loginRequestDTO.getAccessToken());
        User user = userService.getUserByEmail(email);
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("get information user successful", user);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse> refreshAndGetAuthenticationToken(@RequestBody LoginRequestDTO loginRequestDTO) {
        return refreshTokenService.findByToken(loginRequestDTO.getRefreshToken())
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    // Generate a new access token
                    final String jwt = jwtUtil.generateToken(user.getEmail());
                    LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), jwt, refreshToken.getToken());
                    BaseResponse baseResponse = BaseResponse.createSuccessResponse("refresh-token successful", loginResponseDTO);
                    return ResponseEntity.status(200).body(baseResponse);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));
    }
    @PostMapping("/oauth2/login-success")
    public ResponseEntity<BaseResponse> handleOAuth2LoginSuccess(@RequestBody O2authRequestDTO o2authRequestDTO) {
        // Process user info from OAuth2 provider
        User user = oauth2Service.processOAuth2User(o2authRequestDTO);
        // Process access and refresh tokens
        RefreshToken refreshToken = oauth2Service.processOAuth2Tokens(user);
        // Generate JWT token for user
        UsernamePasswordAuthenticationToken userAuthentication =
                new UsernamePasswordAuthenticationToken(user, null);
        String accessToken = jwtUtil.generateToken(user.getEmail());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), accessToken, refreshToken.getToken());
        BaseResponse baseResponse = BaseResponse.createSuccessResponse("login o2auth successful", loginResponseDTO);
        return ResponseEntity.ok(baseResponse);
    }
    @PostMapping("/updatepassword")
    public ResponseEntity<BaseResponse> changeUserPassword(@RequestParam String newPassword, @RequestParam String oldPassword) {
        User user = userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            BaseResponse baseResponse = BaseResponse.createSuccessResponse("update password successful");
            return ResponseEntity.ok(baseResponse);
        } else {
            BaseResponse baseResponse = BaseResponse.createErrorResponse("update password");
            return ResponseEntity.status(400).body(baseResponse);
        }
    }
}
