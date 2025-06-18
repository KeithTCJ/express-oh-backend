package com.team_one.expressoh.config;

import com.team_one.expressoh.service.EcommerceUserDetailsService;
import com.team_one.expressoh.service.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private EcommerceUserDetailsService ecommerceUserDetailsService;

    public JwtAuthenticationFilter(EcommerceUserDetailsService ecommerceUserDetailsService) {
        this.ecommerceUserDetailsService = ecommerceUserDetailsService;
    }

    /**
     * For each HTTP request, it checks for a JWT in the Authorization header,
     * validates the token, and if valid, establishes an authentication object in the SecurityContext.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        System.out.println("[JwtAuthFilter] Authorization header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[JwtAuthFilter] No Bearer token found, continuing filter chain.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);
        final String userEmail = jwtUtils.extractUsername(jwtToken);
        System.out.println("[JwtAuthFilter] Extracted userEmail from token: " + userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = ecommerceUserDetailsService.loadUserByUsername(userEmail);
            System.out.println("[JwtAuthFilter] Loaded UserDetails: " + userDetails.getUsername());
            System.out.println("[JwtAuthFilter] User authorities: " + userDetails.getAuthorities());

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("[JwtAuthFilter] SecurityContext updated with authenticated user.");
            } else {
                System.out.println("[JwtAuthFilter] JWT token is NOT valid.");
            }
        } else {
            System.out.println("[JwtAuthFilter] userEmail is null or SecurityContext already has authentication.");
        }

        filterChain.doFilter(request, response);
    }
}