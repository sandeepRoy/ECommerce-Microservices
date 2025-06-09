package com.msa.authentication.services;

import com.msa.authentication.entities.CustomUserDetails;
import com.msa.authentication.entities.Role;
import com.msa.authentication.entities.User;
import com.msa.authentication.repositories.UserRepository;
import com.msa.authentication.requests.*;
import com.msa.authentication.responses.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public Logger logger = Logger.getLogger(AuthenticationService.class.getName());

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public TokenBlacklistService tokenBlacklistService;

    @Autowired
    public AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest registerRequest) {
        User user = User
                .builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        User save = userRepository.save(user);
        CustomUserDetails customUserDetails = new CustomUserDetails(save);
        String access_token = jwtService.generateAccessToken(customUserDetails);
        String refresh_token = jwtService.generateRefreshToken(customUserDetails);

        AuthResponse authResponse = AuthResponse.builder().access_token(access_token).refresh_token(refresh_token).build();
        return authResponse;
    }

    public AuthResponse authenticate(AuthenticateRequest authenticateRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(), authenticateRequest.getPassword())
        );
        User user = userRepository.findUserByEmail(authenticateRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String access_token = jwtService.generateAccessToken(customUserDetails);
        String refresh_token = jwtService.generateRefreshToken(customUserDetails);

        AuthResponse authResponse = AuthResponse.builder().access_token(access_token).refresh_token(refresh_token).build();
        return authResponse;
    }

    public AuthResponse otpLogin(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        User user;
        if(userByEmail.isPresent()) {
            user = userByEmail.get();
        }
        else {
            user = User
                    .builder()
                    .email(email)
                    .password(passwordEncoder.encode(email)) // Mobile Number as email, OTP as password?
                    .firstname("not_provided")
                    .lastname("not_provided")
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        AuthResponse authResponse = AuthResponse.builder().access_token(accessToken).refresh_token(refreshToken).build();
        return authResponse;
    }

    public String remove(Authentication authentication) {
        String user_email = authentication.getName();
        User userFound = userRepository.findUserByEmail(user_email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        userRepository.delete(userFound);
        return "User Deleted";
    }

    public Boolean validateToken(Authentication authentication) {
        String user_email = authentication.getName();
        logger.info("user_email: " + user_email);
        return user_email != null && !user_email.isEmpty();
    }

    public User updateName(Authentication authentication, UpdateNameRequest updateNameRequest) {
        String user_email = authentication.getName();
        User user = userRepository.findUserByEmail(user_email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setFirstname(updateNameRequest.getFirst_name());
        user.setLastname(updateNameRequest.getLast_name());
        userRepository.save(user);
        return user;
    }

    public User updateEmailAndPassword(Authentication authentication, UpdateEmailAndPasswordRequest updateEmailAndPasswordRequest) {
        String user_email = authentication.getName();
        User user = userRepository.findUserByEmail(user_email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setEmail(updateEmailAndPasswordRequest.getEmail());
        userRepository.save(user);
        return user;
    }

    public User changePassword(Authentication authentication, ChangePasswordRequest changePasswordRequest) {
        String user_email = authentication.getName();
        User user = userRepository.findUserByEmail(user_email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        userRepository.save(user);
        return user;
    }

    public AuthResponse refreshAccessToken(String refresh_token) {
        if(tokenBlacklistService.isRefreshTokenBlacklisted(refresh_token) == true) {
            logger.warning("Refresh token has been blacklisted");
            return AuthResponse.builder().access_token("TOKEN BLACKLISTED").refresh_token("TOKEN BLACKLISTED").build();
        }
        String userId = jwtService.extractUserIdFromRefreshToken(refresh_token); logger.info("AuthenticationService.Logged In User ID: " + userId);
        User user = userRepository
                .findById(Integer.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        log.info("AuthenticationService.Logged In User: " +  user.toString());

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        if(jwtService.isRefreshTokenValid(refresh_token, user)) {
            logger.warning("Refresh token has been validated");
            String access_token = jwtService.generateAccessToken(customUserDetails);
            return AuthResponse.builder().access_token(access_token).refresh_token(refresh_token).build();
        }
        else {
            logger.warning("Refresh token hasn't been validated");
            return AuthResponse.builder().access_token("Unauthorized Access!").build();
        }
    }
}

