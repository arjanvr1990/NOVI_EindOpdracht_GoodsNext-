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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authentication/authenticate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/contactinfo").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/contactinfo/me").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/contactinfo/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/contactinfo/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/contactinfo/me").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/contactinfo/me").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/api/shops/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/shops").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/shops/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/shops/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/shops/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/photos/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/photos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/photos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/photos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/photos/**").hasRole("ADMIN")



                        .anyRequest().authenticated()


                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
