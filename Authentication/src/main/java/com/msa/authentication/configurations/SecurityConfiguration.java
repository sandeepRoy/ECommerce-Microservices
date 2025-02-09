package com.msa.authentication.configurations;

import com.msa.authentication.handlers.CustomOAuthSucessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Autowired
    public JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    public AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()                                                                             // cross browser restriction disabled
                .authorizeHttpRequests(authorize -> authorize                                                 // authorize each request, by finding the url pattern
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()         // some open endpoint/s
                        .requestMatchers("/oauth/**").authenticated())                                        // some authenticated endpoint/s
                .oauth2Login(oauth2 -> oauth2.successHandler(new CustomOAuthSucessHandler()))                 // requires oauth consent screen, handles : Line 16 : CustomOAuthSucessHandler
                .sessionManagement(session -> session                                                         // authentication requires a session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))                                 // create a session and keep it on
                .authenticationProvider(authenticationProvider)                                               // spring's security class to do this processing
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);                  // send unfiltered request for JWT processing
        return httpSecurity.build();                                                                          // return the request to HTTP
    }
}


