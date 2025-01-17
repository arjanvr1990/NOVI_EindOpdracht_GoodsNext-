package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Haal de UserDto op via de UserService
        UserDto userDto = userService.getUserByUsername(username);

        if (userDto == null) {
            throw new RuntimeException("User not found: " + username);
        }

        // Wachtwoord van de gebruiker
        String password = userDto.getPassword();

        // Authorities ophalen en omzetten naar GrantedAuthority
        Set<String> authorities = userDto.getAuthorities();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority)); // Gebruik de authority zoals die is
        }

        // Maak en retourneer een Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(username, password, grantedAuthorities);
    }
}
