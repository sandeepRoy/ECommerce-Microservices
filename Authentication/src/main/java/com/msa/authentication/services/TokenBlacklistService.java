package com.msa.authentication.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    private final Set<String> accessToken_blacklist = ConcurrentHashMap.newKeySet();
    private final Set<String> refreshToken_blacklist = ConcurrentHashMap.newKeySet();

    public void blacklistAccessToken(String access_token) {
        accessToken_blacklist.add(access_token);
    }

    public boolean isAccessTokenBlacklisted(String access_token) {
        return accessToken_blacklist.contains(access_token);
    }

    public void blacklistRefreshToken(String refresh_token) {
        refreshToken_blacklist.add(refresh_token);
    }

    public boolean isRefreshTokenBlacklisted(String refresh_token) {
        return refreshToken_blacklist.contains(refresh_token);
    }
}
