package com.sp.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data // Brings back your getters, setters, and constructors
@Document(collection = "users")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id // Correct import: org.springframework.data.annotation.Id
    private String id; // String is best for MongoDB auto-generated IDs

    private String name;
    private int age;
    private String username;
    private String email;

    @JsonIgnore
    private String password;

    public User(final String username,final String password, final String email, final String name, final int age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    // =======================================================================
    // REQUIRED USERDETAILS METHODS (This makes the red lines go away)
    // =======================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Grants every user a default role. 
        // Later, you can load real roles from the database here.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // The following methods MUST return 'true' for now. 
    // If they return false, Spring Security will block the user from logging in!

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}