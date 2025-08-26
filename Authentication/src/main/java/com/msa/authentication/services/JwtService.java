package com.msa.authentication.services;

import com.msa.authentication.entities.CustomUserDetails;
import com.msa.authentication.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;
import java.util.*;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.access-secret}")
    private String access_key;

    @Value("${jwt.refresh-secret}")
    private String refresh_key;

    @Value("${jwt.access-expiry}")
    private long access_expiryDate;

    @Value("${jwt.refresh-expiry}")
    private long refresh_expiryDate;

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(access_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Key getRefreshSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refresh_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractRefreshClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllRefreshClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims extractAllRefreshClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getRefreshSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserIdFromRefreshToken(String token) {
        return extractRefreshClaim(token, Claims::getSubject);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, CustomUserDetails userDetails) {
        User user = ((CustomUserDetails) userDetails).getUser();

        return Jwts.builder()
                .setClaims(extraClaims)
                .claim("email", user.getEmail())
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + access_expiryDate))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String generateAccessToken(CustomUserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        User user = ((CustomUserDetails) userDetails).getUser();

        String refresh_token = Jwts
                .builder()
                .setClaims(extraClaims)
                .claim("email", user.getEmail())
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refresh_expiryDate))
                .signWith(getRefreshSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return refresh_token;
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        User user = ((CustomUserDetails) userDetails).getUser();

        if (!String.valueOf(user.getId()).equals(userId)) { return false; }
        if (isTokenExpired(token)) { return false; }

        // block if password expired
        if(user.getPasswordExpiryDate() != null && LocalDateTime.now().isAfter(user.getPasswordExpiryDate())) {
            return false;
        }

        // block tokens issued before last password change
        Date issuedAt = extractIssuedAt(token);
        if(user.getPasswordChangedAt() != null && issuedAt != null) {
            if(issuedAt.toInstant().isBefore(user.getPasswordChangedAt().atZone(ZoneId.systemDefault()).toInstant())) {
                return false;
            }
        }
        return true;
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        String subject = extractRefreshClaim(token, Claims::getSubject);
        User user = (User) userDetails;

        if (!String.valueOf(user.getId()).equals(subject)) return false;
        if (isRefreshTokenExpired(token)) return false;

        // Same two rules for refresh
        if (user.getPasswordExpiryDate() != null && LocalDateTime.now().isAfter(user.getPasswordExpiryDate())) {
            return false;
        }
        Date iat = extractRefreshIssuedAt(token);
        if (user.getPasswordChangedAt() != null && iat != null) {
            if (iat.toInstant().isBefore(user.getPasswordChangedAt().atZone(ZoneId.systemDefault()).toInstant())) {
                return false;
            }
        }
        return true;
    }


    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }




    public boolean isRefreshTokenExpired(String token) {
        return extractRefreshClaim(token, Claims::getExpiration).before(new Date());
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public Date extractRefreshIssuedAt(String token) {
        return extractRefreshClaim(token, Claims::getIssuedAt);
    }


}
