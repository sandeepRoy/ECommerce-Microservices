package com.msa.inventory.service;

import com.msa.inventory.clients.AuthenticationClient;
import com.msa.inventory.requests.authentication.RegistrationRequest;
import com.msa.inventory.responses.authentication.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    public AuthenticationClient authenticationClient;

    public AuthenticationResponse registerAdmin(RegistrationRequest registrationRequest) {
        return authenticationClient.register(registrationRequest).getBody();
    }

    public AuthenticationResponse loginAdmin(RegistrationRequest registrationRequest) {
        return authenticationClient.login(registrationRequest).getBody();
    }
}
