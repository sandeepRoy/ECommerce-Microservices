package com.msa.seller.services;

import com.msa.seller.clients.AuthenticationClient;
import com.msa.seller.clients.UserClient;
import com.msa.seller.entities.Seller;
import com.msa.seller.repositories.SellerRepository;
import com.msa.seller.responses.profile.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    public SellerRepository sellerRepository;

    @Autowired
    public UserClient userClient;

    public UserProfileResponse getSellerProfile(String access_token) {
        return userClient.getLoggedInUser(access_token).getBody();
    }
}
