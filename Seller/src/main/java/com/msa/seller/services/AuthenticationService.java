package com.msa.seller.services;

import com.msa.seller.clients.AuthenticationClient;
import com.msa.seller.clients.UserClient;
import com.msa.seller.entities.Seller;
import com.msa.seller.repositories.SellerRepository;
import com.msa.seller.requests.authentication.RegistrationRequest;
import com.msa.seller.responses.authentication.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    public AuthenticationClient authenticationClient;

    @Autowired
    public SellerRepository sellerRepository;

    public AuthenticationResponse registerSeller(RegistrationRequest registrationRequest) {

        Optional<Seller> optionalSeller = sellerRepository.findOne(Example.of(Seller.builder().seller_email(registrationRequest.getEmail()).build()));
        if (optionalSeller.isEmpty()) {
            AuthenticationResponse authenticationResponse = authenticationClient.register(registrationRequest).getBody();
            sellerRepository.save(Seller.builder().seller_email(registrationRequest.getEmail()).build());
            return authenticationResponse;
        }
        return AuthenticationResponse.builder().message("SELLER EXISTS").build();
    }

    public AuthenticationResponse loginSeller(RegistrationRequest registrationRequest) {
        Optional<Seller> optionalSeller = sellerRepository.findOne(Example.of(Seller.builder().seller_email(registrationRequest.getEmail()).build()));

        if(optionalSeller.isPresent()) {
            return authenticationClient.login(registrationRequest).getBody();
        }
        return AuthenticationResponse.builder().message("SELLER DOESN'T EXISTS").build();
    }
}
