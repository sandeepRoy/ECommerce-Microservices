package com.msa.inventory.clients;

import com.msa.inventory.responses.profile.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user", url = "http://localhost:8086/user")
public interface UserClient {
    @GetMapping("/get-loggedIn-user")
    public ResponseEntity<UserProfileResponse> getLoggedInUser(@RequestHeader("Authorization") String token);
}
