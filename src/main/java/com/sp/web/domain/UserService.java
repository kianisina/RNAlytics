package com.sp.web.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface  UserService extends UserDetailsService {
    User registerUser(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    List<User> getUsers();
    Optional<User> getUser(String id);
    Optional<User> getUserByEmail(String email);
    void saveUser(User user);

}
