package com.msa.customer.clients;

import com.msa.customer.dtos.UpdateEmailDto;
import com.msa.customer.dtos.UpdateNameDto;
import com.msa.customer.dtos.UpdatePasswordDto;
import com.msa.customer.responses.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user", url = "http://localhost:8084/user")
public interface UserClient {

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestHeader("Authorization") String token);

    @GetMapping("/get-loggedIn-user")
    public ResponseEntity<UserProfileResponse> getLoggedInUser(@RequestHeader("Authorization") String token);

    @PutMapping("/update-name")
    public ResponseEntity<UserProfileResponse> update(@RequestHeader("Authorization") String token, @RequestBody UpdateNameDto updateNameDto);

    @PutMapping("/update-email-and-password")
    public ResponseEntity<UserProfileResponse> update(@RequestHeader("Authorization") String token, @RequestBody UpdateEmailDto updateEmailDto);

    @PutMapping("/update-password")
    public ResponseEntity<UserProfileResponse> update(@RequestHeader("Authorization") String token, @RequestBody UpdatePasswordDto updatePasswordDto);

    @DeleteMapping("/remove")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token);
}

