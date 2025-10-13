package com.msa.authentication.services;

import com.msa.authentication.entities.CustomUserDetails;
import com.msa.authentication.entities.Role;
import com.msa.authentication.entities.User;
import com.msa.authentication.handlers.CustomOAuthSucessHandler;
import com.msa.authentication.repositories.UserRepository;
import com.msa.authentication.requests.RegisterRequest;
import com.msa.authentication.responses.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/*
* Approach
* 1. For existing user, bypass username and password based login, just generate token
* 2. For new user, record the entry and provide the token
* */
@Service
public class OAuthService {

    private Logger logger = Logger.getLogger(OAuthService.class.getName());

    @Autowired
    public AuthenticationService authenticationService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public JwtService jwtService;

    public AuthResponse registerAndLoginOAuthUser() {
        List<String> oAuthUserDetails = CustomOAuthSucessHandler.getOAuthUserDetails();

        String name = oAuthUserDetails.get(0);
        String email = oAuthUserDetails.get(1);
        String password = oAuthUserDetails.get(2);

        User user = userRepository.findUserByEmail(email).orElse(
                User.builder().firstname("NOT_SET").lastname("NOT_SET").email(email).role(Role.USER).password(password).build()
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        if(user.getFirstname() == "NOT_SET") {
            return authenticationService.register(createRegisterRequest(name, email, password));
        }
        else {
            if(user.getPasswordExpiryDate() == LocalDateTime.now()) {
                AuthResponse authResponse = AuthResponse.builder().access_token("NOT_GENERATED").refresh_token("NOT_GENERATED").message("PASSWORD_EXPIRED").build();
                return authResponse;
            }
            user.setPasswordExpiryDate(LocalDateTime.now().plusMinutes(30));
            userRepository.save(user);
            String accessToken = jwtService.generateAccessToken(customUserDetails);
            String refreshToken = jwtService.generateRefreshToken(customUserDetails);
            return AuthResponse.builder().access_token(accessToken).refresh_token(refreshToken).message("PASSWORD_ACTIVE").build();
        }
    }

    public RegisterRequest createRegisterRequest(String name, String email, String password) {
        RegisterRequest registerRequest = RegisterRequest
                .builder()
                .firstname(name.substring(0, name.indexOf(' ')))
                .lastname(name.substring(name.indexOf(' ') + 1, name.length()))
                .email(email)
                .password(password)
                .build();
        return registerRequest;
    }
}
