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
    public ResponseEntity<AuthResponse> loginSuccess(HttpServletResponse httpServletResponse) throws IOException {
        AuthResponse authResponse = oAuthService.registerAndLoginOAuthUser();

        Cookie accessTokenCookie = new Cookie("ACCESS_TOKEN", authResponse.getAccess_token());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60 * 24);

        Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", authResponse.getRefresh_token());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24);

        httpServletResponse.addCookie(accessTokenCookie);
        httpServletResponse.addCookie(refreshTokenCookie);
        httpServletResponse.sendRedirect("http://localhost:8087/customer/auth/social-callback");

        return new ResponseEntity<>(authResponse, HttpStatus.OK); // try loggin
    }
}


