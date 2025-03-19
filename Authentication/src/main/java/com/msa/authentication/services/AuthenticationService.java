package com.msa.authentication.services;

import com.msa.authentication.repositories.UserRepository;
import com.msa.authentication.requests.UpdateNameRequest;
import com.msa.authentication.responses.AuthResponse;
import com.msa.authentication.requests.AuthenticateRequest;
import com.msa.authentication.requests.RegisterRequest;
import com.msa.authentication.entities.Role;
import com.msa.authentication.entities.User;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

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
        String token = jwtService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder().token(token).build();
        return authResponse;
    }

    public AuthResponse authenticate(AuthenticateRequest authenticateRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(), authenticateRequest.getPassword())
        );
        User user = userRepository.findUserByEmail(authenticateRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder().token(token).build();
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
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .firstname("not_provided")
                    .lastname("not_provided")
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
        }
        String token = jwtService.generateToken(user);
        AuthResponse authResponse = AuthResponse.builder().token(token).build();
        return authResponse;
    }

    public String remove(String token) {
        String user_email = jwtService.extractUsername(token);
        User userFound = userRepository.findUserByEmail(user_email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        userRepository.delete(userFound);
        return "User Deleted";
    }

    public Boolean validateToken(String token) {
        try {

            String username = jwtService.extractUsername(token);
            boolean isValid = jwtService.isTokenExpired(token);
            return isValid;
        } catch (Exception e) { return false;}
    }

    public String updateName(String email, UpdateNameRequest updateNameRequest) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        user.setFirstname(updateNameRequest.getFirst_name());
        user.setLastname(updateNameRequest.getLast_name());
        userRepository.save(user);
        return "Updated";
    }
}
