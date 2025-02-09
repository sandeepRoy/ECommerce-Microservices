package com.msa.authentication.services;

import com.msa.authentication.entities.Role;
import com.msa.authentication.entities.User;
import com.msa.authentication.handlers.CustomOAuthSucessHandler;
import com.msa.authentication.repositories.UserRepository;
import com.msa.authentication.requests.AuthenticateRequest;
import com.msa.authentication.requests.RegisterRequest;
import com.msa.authentication.responses.AuthResponse;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
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
        String at_hash = oAuthUserDetails.get(2);

        User user = userRepository.findUserByEmail(email).orElse(
                User.builder().firstname("NOT_SET").lastname("NOT_SET").email(email).password(at_hash).build()
        );

        if(user.getFirstname() == "NOT_SET") {
            return authenticationService.register(createRegisterRequest(name, email, at_hash));
        }
        else {
            String token = jwtService.generateToken(user);
            return AuthResponse.builder().token(token).build();
        }
    }

    public RegisterRequest createRegisterRequest(String name, String email, String at_hash) {
        RegisterRequest registerRequest = RegisterRequest
                .builder()
                .firstname(name.substring(0, name.indexOf(' ')))
                .lastname(name.substring(name.indexOf(' ') + 1, name.length()))
                .email(email)
                .password(at_hash)
                .build();
        return registerRequest;
    }
}
