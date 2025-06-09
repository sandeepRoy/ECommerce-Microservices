package com.msa.authentication.controller;

import com.msa.authentication.entities.CustomUserDetails;
import com.msa.authentication.entities.User;
import com.msa.authentication.requests.ChangePasswordRequest;
import com.msa.authentication.requests.UpdateEmailAndPasswordRequest;
import com.msa.authentication.requests.UpdateNameRequest;
import com.msa.authentication.responses.UserProfileResponse;
import com.msa.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public AuthenticationService authenticationService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(Authentication authentication) {
        Boolean response = authenticationService.validateToken(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-loggedIn-user")
    public ResponseEntity<UserProfileResponse> getLoggedInUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setFirstName(user.getFirstname());
        userProfileResponse.setLastName(user.getLastname());

        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    @PutMapping("/update-name")
    public ResponseEntity<UserProfileResponse> update(Authentication authentication, @RequestBody UpdateNameRequest updateNameRequest) {
        User user = authenticationService.updateName(authentication, updateNameRequest);

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(user.getUsername());
        userProfileResponse.setFirstName(user.getFirstname());
        userProfileResponse.setLastName(user.getLastname());

        return new ResponseEntity<>(userProfileResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update-email-and-password")
    public ResponseEntity<UserProfileResponse> update(Authentication authentication, @RequestBody UpdateEmailAndPasswordRequest updateEmailAndPasswordRequest) {
        User user = authenticationService.updateEmailAndPassword(authentication, updateEmailAndPasswordRequest);

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(user.getUsername());
        userProfileResponse.setFirstName(user.getFirstname());
        userProfileResponse.setLastName(user.getLastname());

        return new ResponseEntity<>(userProfileResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update-password")
    public ResponseEntity<UserProfileResponse> update(Authentication authentication, @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = authenticationService.changePassword(authentication, changePasswordRequest);

        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setEmail(user.getUsername());
        userProfileResponse.setFirstName(user.getFirstname());
        userProfileResponse.setLastName(user.getLastname());

        return new ResponseEntity<>(userProfileResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> delete(Authentication authentication) {
        String response = authenticationService.remove(authentication);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
