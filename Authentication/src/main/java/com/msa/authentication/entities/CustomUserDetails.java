package com.msa.authentication.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final Integer id;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean isEnabled;
    private final User user;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();

        this.authorities = (List<GrantedAuthority>) user.getAuthorities();

        this.isEnabled = user.isEnabled();
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;  // This is still email, but you will load user by id elsewhere
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // or your logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // or your logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // or your logic
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public User getUser() {
        return user;
    }
}
