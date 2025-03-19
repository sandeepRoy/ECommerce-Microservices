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

/*
Line 33 Explanation :

1. **What was happening before?**
   - Imagine you’re walking to your friend’s house (your `/oauth/login` endpoint).
   - On the way, someone (Spring Security) tells you, “Wait, you need to get a pass from the guard first (Google Login).”
   - So, you go to the guard, get the pass, and return.
   - But instead of letting you into your friend’s house, the guard keeps sending you back to his booth (the login page).

   Why? Because the guard doesn’t know where to send you after you get your pass. 😅

---

2. **What did we change?**
   - We gave the guard clear instructions: “After Sandeep gets the pass, send him to the party at `/oauth/success`.”

---

3. **How did we tell the guard what to do?**
   - We created a helper (the `CustomOAuth2SuccessHandler`), whose job is to redirect you to the party (`/oauth/success`) after you show your pass to the guard.
   - Before, the guard (Spring Security) was following its default rule: “Send Sandeep to the login booth by default.”

---

4. **What else did we fix?**
   - We adjusted how the guard handles visitors.
   - We told the guard to hold on to important instructions during the process (by adjusting session rules), so you wouldn’t get lost or sent back to the wrong place.

---

5. **Why does this work now?**
   - Now, when you visit `/oauth/login`, the guard (Spring Security) makes sure you log in with Google.
   - Once you’re logged in, our helper steps in and says: “Okay, Sandeep, you’re good to go to `/oauth/success`.”
   - And everything works just as it should! 🎉

---

In short:
- Before, the guard didn’t know where to send you.
- Now, we’ve taught the guard how to guide you to the party. 😊

*/


