package com.arjanvanraamsdonk.goodsnext.dtos;

import java.util.Set;

public class UserDto {
    private String username;
    private String password;
    private Set<String> authorities; // Vervang "roles" door "authorities"

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAuthorities() {
        return authorities; // Verander "roles" naar "authorities"
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities; // Verander "roles" naar "authorities"
    }
}
