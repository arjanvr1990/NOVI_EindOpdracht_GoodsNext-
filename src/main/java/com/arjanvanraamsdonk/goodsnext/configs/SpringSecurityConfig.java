package com.arjanvanraamsdonk.goodsnext.configs;

import com.arjanvanraamsdonk.goodsnext.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SpringSecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Nieuwe manier om CSRF uit te schakelen
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authentication/authenticate").permitAll() // Open endpoint voor authenticatie
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll() // Open endpoint voor gebruikersregistratie
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN") // Alleen toegankelijk voor ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN") // Alleen toegankelijk voor ADMIN
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN")



                        .requestMatchers(HttpMethod.GET, "/api/contactinfo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/contactinfo/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/contactinfo/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/contactinfo/me").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/contactinfo/me").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/shops/**").hasAnyRole("USER", "ADMIN") // USER en ADMIN kunnen shops bekijken
                        .requestMatchers(HttpMethod.POST, "/api/shops").hasRole("ADMIN") // Alleen ADMIN kan shops aanmaken
                        .requestMatchers(HttpMethod.PUT, "/api/shops/**").hasRole("ADMIN") // Alleen ADMIN kan shops bijwerken
                        .requestMatchers(HttpMethod.PATCH, "/api/shops/**").hasRole("ADMIN") // Alleen ADMIN kan gedeeltelijke updates uitvoeren



                        .anyRequest().authenticated() // Alle andere endpoints vereisen authenticatie


                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessies instellen
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
