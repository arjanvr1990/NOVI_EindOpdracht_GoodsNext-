package com.arjanvanraamsdonk.goodsnext.filter;

import com.arjanvanraamsdonk.goodsnext.services.CustomUserDetailsService;
import com.arjanvanraamsdonk.goodsnext.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {



    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Haal de "Authorization" header op uit de request
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Controleer of de header correct is en begin met "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                // Extracteer de username uit het token
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                logger.error("JWT Token processing failed: " + e.getMessage());
            }
        }

        // Controleer of de gebruiker nog niet is geauthenticeerd
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Laad de gebruiker op basis van de username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Valideer het token
            if (jwtUtil.validateToken(jwt, userDetails)) {
                // Creëer een authenticatietoken
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Voeg aanvullende details toe aan de authenticatie
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Stel de authenticatie in de SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Ga verder met de filter chain
        filterChain.doFilter(request, response);
    }
}
