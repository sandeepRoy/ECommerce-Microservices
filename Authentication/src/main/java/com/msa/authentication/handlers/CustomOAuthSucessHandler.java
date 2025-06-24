package com.msa.authentication.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class CustomOAuthSucessHandler implements AuthenticationSuccessHandler {

    public static String name = "";
    public static String email = "";
    public static String password = "";

    Logger logger = Logger.getLogger(CustomOAuthSucessHandler.class.getName());

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauth2AuthenticationToken.getPrincipal().getAttributes();

        name = attributes.get("name").toString();
        email = attributes.get("email").toString();
        password = attributes.get("email").toString();

        response.sendRedirect("/oauth/success");
    }

    public static List<String> getOAuthUserDetails() {
        return List.of(name, email, password);
    }
}

