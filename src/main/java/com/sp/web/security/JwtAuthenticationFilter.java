package com.sp.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.web.web.UserCMD;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/login"); // The endpoint your frontend will call to log in
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Read the JSON from the frontend and turn it into our UserCMD object
            UserCMD creds = new ObjectMapper().readValue(request.getInputStream(), UserCMD.class);

            // Pass the username and password to Spring Security to check against the Database
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new java.util.ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        
        // Login was successful! Get the user details.
        UserDetails user = (UserDetails) authResult.getPrincipal();

        // Build the secret key
        SecretKey key = Keys.hmacShaKeyFor(SecurityConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        // Generate the JWT token (JJWT 0.12.5 syntax)
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .signWith(key)
                .compact();

        // Send the token back to the frontend in the HTTP Response Header
        response.addHeader(SecurityConstant.HEADER_STRING, SecurityConstant.TOKEN_PREFIX + token);
        
        // Optional: Also send it in the body so it's easier for the frontend to read
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + SecurityConstant.TOKEN_PREFIX + token + "\"}");
    }
}