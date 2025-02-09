package com.msa.authentication.controller;

import com.msa.authentication.responses.AuthResponse;
import com.msa.authentication.services.OAuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    public Logger logger = Logger.getLogger(OAuthController.class.getName());

    @Autowired
    public OAuthService oAuthService;

    @GetMapping("/login")
    public ResponseEntity<String> socialLogin(@RequestParam String provider) {
        return new ResponseEntity<>("OAuth Consent Completed!", HttpStatus.OK);
    }

    @GetMapping("/success")
    public void loginSuccess(HttpServletResponse httpServletResponse) throws IOException {
        AuthResponse authResponse = oAuthService.registerAndLoginOAuthUser();

        Cookie jwtCookie = new Cookie("JWT_TOKEN", authResponse.getToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24);

        httpServletResponse.addCookie(jwtCookie);
        httpServletResponse.sendRedirect("http://localhost:8087/customer/auth/social-callback");
    }
}


