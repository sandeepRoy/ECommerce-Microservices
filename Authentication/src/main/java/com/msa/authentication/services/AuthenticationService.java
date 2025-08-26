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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public Logger logger = Logger.getLogger(AuthenticationService.class.getName());

    @Value("${user.password-expiry}")
    private Integer password_expiry_in_mins;

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

        user.setPasswordChangedAt(LocalDateTime.now());
        user.setPasswordExpiryDate(LocalDateTime.now().plusMinutes(30));

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

        if(user.getPasswordExpiryDate().isBefore(LocalDateTime.now())) {
            AuthResponse authResponse = AuthResponse.builder().access_token("NOT_GENERATED").refresh_token("NOT_GENERATED").message("PASSWORD_EXPIRED").build();
            return authResponse;
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String access_token = jwtService.generateAccessToken(customUserDetails);
        String refresh_token = jwtService.generateRefreshToken(customUserDetails);

        AuthResponse authResponse = AuthResponse.builder().access_token(access_token).refresh_token(refresh_token).build();
        return authResponse;
    }

    public AuthResponse otpLogin(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        User user;
        if(userByEmail.isPresent() /*&& userByEmail.get().getPasswordExpirationDate() == LocalDateTime.now()*/) {
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

    public AuthResponse refreshAccessToken(String refresh_token) {
        if(tokenBlacklistService.isRefreshTokenBlacklisted(refresh_token) == true) {
            logger.warning("Refresh token has been blacklisted");
            return AuthResponse.builder().access_token("TOKEN BLACKLISTED").refresh_token("TOKEN BLACKLISTED").message("BOTH TOKENS EXPIRED").build();
        }
        String userId = jwtService.extractUserIdFromRefreshToken(refresh_token); logger.info("AuthenticationService.Logged In User ID: " + userId);
        User user = userRepository
                .findById(Integer.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        log.info("AuthenticationService.Logged In User: " +  user.toString());

        if(!jwtService.isRefreshTokenValid(refresh_token, user)) {
            return AuthResponse.builder().access_token("UNAUTHORIZED").refresh_token("UNAUTHORIZED").message("INVALID TOKEN").build();
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        String access_token = jwtService.generateAccessToken(customUserDetails);

        return AuthResponse.builder().access_token(access_token).refresh_token(refresh_token).message("TOKENS REFRESHED").build();
    }

    public AuthResponse changePasswordAfterExpiry(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findUserByEmail(changePasswordRequest.getUser_name()).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        if(!passwordEncoder.matches(changePasswordRequest.getOld_password(), user.getPassword())) {
            return AuthResponse.builder().access_token("NOT_GENERATED").refresh_token("NOT_GENERATED").message("PASSWORD MISMATCH").build();
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNew_password()));
        user.setPasswordChangedAt(LocalDateTime.now());
        user.setPasswordExpiryDate(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        AuthenticateRequest authenticateRequest = new AuthenticateRequest(changePasswordRequest.getUser_name(), changePasswordRequest.getNew_password());

        AuthResponse authResponse = authenticate(authenticateRequest);
        authResponse.setMessage("PASSWORD REFRESHED");

        return authResponse;
    }
}

