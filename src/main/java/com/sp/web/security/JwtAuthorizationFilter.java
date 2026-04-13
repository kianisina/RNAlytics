package com.sp.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        String header = request.getHeader(SecurityConstant.HEADER_STRING);

        // If there's no token, or it doesn't start with "Bearer ", move along.
        if (header == null || !header.startsWith(SecurityConstant.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        // We found a token! Let's verify it.
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        
        // Save the authenticated user to the Spring Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstant.HEADER_STRING);
        if (token != null) {
            try {
                // Remove the "Bearer " prefix
                String jwtToken = token.replace(SecurityConstant.TOKEN_PREFIX, "");
                
                // Build the secret key
                SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

                // Parse the token using JJWT 0.12.5 syntax
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();

                String username = claims.getSubject();

                if (username != null) {
                    // We successfully verified the user! (Normally you'd extract roles here too)
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }
            } catch (Exception e) {
                // Token is expired or tampered with
                return null;
            }
        }
        return null;
    }
}