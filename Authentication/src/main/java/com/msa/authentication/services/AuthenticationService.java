package com.msa.authentication.services;

import com.msa.authentication.repositories.UserRepository;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

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

        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse authenticate(AuthenticateRequest authenticateRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticateRequest.getEmail(), authenticateRequest.getPassword())
        );
        User user = userRepository.findUserByEmail(authenticateRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user);

        return AuthResponse.builder().token(token).build();
    }

    public String remove(String token) {
        String user_email = jwtService.extractUsername(token);
        User userFound = userRepository.findUserByEmail(user_email).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        userRepository.delete(userFound);

        return "User Deleted";
    }

    public Boolean validateToken(String token) {
        // Parse the token and validate it's signature and expiration
        try {

            String username = jwtService.extractUsername(token);
            boolean isValid = jwtService.isTokenExpired(token);
            return isValid;
        } catch (Exception e) { return false;}
    }
}
