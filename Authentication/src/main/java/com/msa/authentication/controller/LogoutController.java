package com.msa.authentication.controller;

import com.msa.authentication.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public LogoutController(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/invoke")
    public ResponseEntity<String> invoke(HttpServletRequest request, @RequestBody String refresh_token) {
        String access_token = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            access_token = authorizationHeader.substring(7);
        }
        if (access_token != null && refresh_token != null) {
            tokenBlacklistService.blacklistAccessToken(access_token);
            tokenBlacklistService.blacklistRefreshToken(refresh_token);
        }
        return new ResponseEntity<>("Logout Successful", HttpStatus.OK);
    }
}

