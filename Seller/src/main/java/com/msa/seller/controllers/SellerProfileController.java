package com.msa.seller.controllers;

import com.msa.seller.responses.profile.UserProfileResponse;
import com.msa.seller.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerProfileController {

    @Autowired
    public ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getSellerProfileInformation(
            @RequestHeader(value = "Authorization") String access_token
    ) {
        UserProfileResponse sellerProfile = profileService.getSellerProfile(access_token);
        return new ResponseEntity(sellerProfile, HttpStatus.OK);
    }
}
