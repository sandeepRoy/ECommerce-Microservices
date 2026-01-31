package com.msa.seller.clients;

import com.msa.seller.responses.profile.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user", url = "http://localhost:8086/user")
public interface UserClient {
    @GetMapping("/get-loggedIn-user")
    public ResponseEntity<UserProfileResponse> getLoggedInUser(@RequestHeader("Authorization") String token);
}
